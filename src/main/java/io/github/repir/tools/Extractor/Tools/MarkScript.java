package io.github.repir.tools.Extractor.Tools;

import io.github.repir.tools.Extractor.Entity;
import io.github.repir.tools.Extractor.Extractor;
import io.github.repir.tools.ByteSearch.ByteRegex;
import io.github.repir.tools.ByteSearch.ByteSearch;
import io.github.repir.tools.ByteSearch.ByteSearchPosition;
import io.github.repir.tools.ByteSearch.ByteSection;
import io.github.repir.tools.Lib.Log;
import java.util.ArrayList;

/**
 * Marks <script> </script> sections, which mark scripts within HTML pages.
 * <p/>
 * @author jbpvuurens
 */
public class MarkScript extends SectionMarker {

   public static Log log = new Log(MarkScript.class);
   public ByteSearch endmarker = new ByteSection("</script", ">");

   public MarkScript(Extractor extractor, String inputsection, String outputsection) {
      super(extractor, inputsection, outputsection);
   }

   @Override
   public ByteRegex getStartMarker() {
      return new ByteRegex("<script");
   }

   @Override
   public void process(Entity entity, int sectionstart, int sectionend, ByteSearchPosition position) {
      int tagclose = findQuoteSafeTagEnd(entity, position.end, sectionend) + 1;
      if (tagclose > -1) {
         ByteSearchPosition end = endmarker.findPos(entity.content, position.end, sectionend);
         if (end.start > position.end) {
            entity.addSectionPos(outputsection, position.start, tagclose, end.start, end.end);
         }
      }
   }
}
