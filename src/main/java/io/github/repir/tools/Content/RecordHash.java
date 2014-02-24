package io.github.repir.tools.Content;

import io.github.repir.tools.Lib.Log;

public class RecordHash extends RecordBinary {
   public static Log log = new Log( RecordHash.class );
   public RecordSort values;
   public int currentbucketindex = -1;
   public LongField offset = this.addLong("offset");

   public RecordHash(Datafile df, int tablesize) {
      super(df);
      this.setCapacity(tablesize);
      this.values = values;
   }

   @Override
   public void openWrite() {
      super.openWrite();
      currentbucketindex = -1;
   }

   @Override
   public void closeWrite() {
      for (; currentbucketindex < bucketcapacity - 1; currentbucketindex++) {
         this.offset.write(0l);
      }
      super.closeWrite();
   }

   public void writeHash(int bucketindex, long offset) {
      for (; currentbucketindex < bucketindex - 1; currentbucketindex++) {
         this.offset.write(-1l);
      }
      if (currentbucketindex < bucketindex) {
         this.offset.write(offset);
         currentbucketindex++;
      } else if (bucketindex < currentbucketindex) {
         log.info("warning trying to write overdue file %s current %d bucket %d offset %d", datafile.getFilename(), currentbucketindex, bucketindex, offset);
      }
   }
}