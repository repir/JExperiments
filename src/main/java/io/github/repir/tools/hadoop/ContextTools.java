package io.github.repir.tools.hadoop;

import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.TaskType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 *
 * @author jeroen
 */
public enum ContextTools {

    ;

    public static io.github.repir.tools.hadoop.Conf getConfiguration(TaskAttemptContext context) {
        return new io.github.repir.tools.hadoop.Conf(context.getConfiguration());
    }

    public static int getTaskID(TaskAttemptContext context) {
        TaskAttemptID taskAttemptID = context.getTaskAttemptID();
        TaskType taskType = taskAttemptID.getTaskType();
        return taskAttemptID.getTaskID().getId();
    }

    public static int getAttemptID(TaskAttemptContext context) {
        TaskAttemptID taskAttemptID = context.getTaskAttemptID();
        return taskAttemptID.getId();
    }

    public static boolean isMap(TaskAttemptContext context) {
        TaskAttemptID taskAttemptID = context.getTaskAttemptID();
        TaskType taskType = taskAttemptID.getTaskType();
        return taskType == TaskType.MAP;
    }

    public static boolean isReduce(TaskAttemptContext context) {
        TaskAttemptID taskAttemptID = context.getTaskAttemptID();
        TaskType taskType = taskAttemptID.getTaskType();
        return taskType == TaskType.REDUCE;
    }

    public static boolean isLastAttempt(TaskAttemptContext context) {
        if (isMap(context)) {
            int maxattempts = context.getConfiguration().getInt(ConfSetting.MAP_MAX_ATTEMPTS.toString(), 0);
            return getAttemptID(context) == maxattempts - 1;
        }
        if (isReduce(context)) {
            int maxattempts = context.getConfiguration().getInt(ConfSetting.REDUCE_MAX_ATTEMPTS.toString(), 0);
            return getAttemptID(context) == maxattempts - 1;
        }
        return false;
    }

    public static boolean isMapSpeculative(TaskAttemptContext context) {
        return context.getConfiguration().getBoolean(MRJobConfig.MAP_SPECULATIVE, true);
    }

    public static boolean isReduceSpeculative(TaskAttemptContext context) {
        return context.getConfiguration().getBoolean(MRJobConfig.REDUCE_SPECULATIVE, true);
    }

    public static Path getInputPath(org.apache.hadoop.mapreduce.Mapper.Context context) {
        return getInputFileSplit(context).getPath();
    }

    public static FileSplit getInputFileSplit(org.apache.hadoop.mapreduce.Mapper.Context context) {
        return ((FileSplit) context.getInputSplit());
    }
}
