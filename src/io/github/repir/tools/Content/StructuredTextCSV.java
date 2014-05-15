package io.github.repir.tools.Content;

import io.github.repir.tools.ByteSearch.ByteSearch;
import io.github.repir.tools.ByteSearch.ByteSearchSection;
import io.github.repir.tools.ByteSearch.ByteSection;
import io.github.repir.tools.Lib.Log;
import java.util.ArrayList;

/**
 * Processes text data, with the elements in a fixed ordered sequence. In contrast
 * to {@link StructuredTextFile}, the elements are not necessarily identified by
 * their context, but by their position. Therefore, all elements must receive a value.
 * This can be used to read and write records with a simple separator such as a 
 * comma or a space, and records separated by an end of line. 
 * <p/>
 * @author jeroen
 */
public abstract class StructuredTextCSV extends StructuredTextFile {

   public static Log log = new Log(StructuredTextCSV.class);
   String open;
   String close;
   ByteSearch regex_open;
   ByteSearch regex_close;

   public StructuredTextCSV(BufferReaderWriter reader) {
      this( reader, "", "[\\t ]+", "", "\t");
   }

   public StructuredTextCSV(BufferReaderWriter reader, String regexopen, String regexclose, String open, String close) {
      super( reader );
      this.open = open;
      this.close = close;
      regex_open = ByteSearch.create(regexopen);
      regex_close = ByteSearch.create(regexclose);
   }

   public StructuredTextCSV(Datafile writer) {
      this( writer, "", "[\\t ]+", "", "\t");
   }

   public StructuredTextCSV(Datafile datafile, String regexopen, String regexclose, String open, String close) {
      super( datafile );
      this.open = open;
      this.close = close;
      regex_open = ByteSearch.create(regexopen);
      regex_close = ByteSearch.create(regexclose);
   }

   protected ByteSearchSection findSection(ByteSearchSection section, ByteSection needle) {
      return section.findSectionDontMove(needle);
   }

   protected class OrderedNode extends FolderNode {

      protected OrderedNode(FolderNode parent, String label, ByteSearch open, ByteSearch close, String openlabel, String closelabel) {
         super(parent, label, open, close, openlabel, closelabel);
      }

      @Override
      protected void readNode(ByteSearchSection section) {
         if (nodevalues == null)
            nodevalues = new ArrayList<NodeValue>();
         nodevalue = new NodeValue();
         for (Node f : orderedfields) {
            ByteSearchSection pos = findSection(section, f.section);
            if (pos.found()) {
               f.readNode(pos);
               section.movePast(pos);
            }
         }
         if (nodevalue.size() > 0)
            nodevalues.add(nodevalue);
      }
      
      @Override
      protected void write(ArrayList list) {
         if (list != null) {
            for (NodeValue v : (ArrayList<NodeValue>) list) {
               if (openlabel.length() > 0) {
                  datafile.printf("%s", openlabel);
               }
               for (Node f : orderedfields) {
                  ArrayList subvalues = v.get(f.label);
                  if (subvalues != null) {
                     f.write(subvalues);
                  } else {
                     log.fatal("Attempted to write an OrderedNode with value %s unset", f.label);
                  }
               }
               if (closelabel.length() > 0) {
                  datafile.printf("%s", closelabel);
               }
            }
         }
      }
   }

   @Override
   public FolderNode addNode(FolderNode parent, String label, ByteSearch open, ByteSearch close, String openlabel, String closelabel) {
      return new OrderedNode(parent, label, open, close, openlabel, closelabel);
   }

   public FolderNode addRoot() {
      return addRoot("root" , "", "\\s*($|\n)", "", "\n");
   }

   public StringField addString(String label) {
      return addString(root, label, regex_open, regex_close, open, close);
   }

   public DoubleField addDouble(String label) {
      return addDouble(root, label, regex_open, regex_close, open, close);
   }

   public IntField addInt(String label) {
      return addInt(root, label, regex_open, regex_close, open, close);
   }

   public LongField addLong(String label) {
      return addLong(root, label, regex_open, regex_close, open, close);
   }
}
