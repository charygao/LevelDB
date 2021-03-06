package cn.edu.nju.client;

import cn.edu.nju.LevelDB;
import cn.edu.nju.file.Key;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GenerationManager {

  private final Path instanceGenerationPath;
  private final Generation generation;

  public GenerationManager(FileSystem fileSystem, Path generationPath)
      throws IOException {
    //generationPath="/generation", dataPath="/data"，可能需要有指向索引的索引，这里暂时不考虑这么复杂，不使用dataPath
    generation = new Generation();
    this.instanceGenerationPath = generationPath;
    FileStatus[] listStatus = fileSystem.listStatus(generationPath);
    for (FileStatus status : listStatus) {
      addRanges(status, fileSystem);
    }
  }

  public void addRange(Key startKey, Key endKey, Path dataPath) {
    generation.ranges.add(new Range(dataPath, startKey, endKey));
  }

  public void flush(FileSystem fileSystem, Range range){
    try {
      write(fileSystem, this.instanceGenerationPath, getRandomRangeName(), range);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<Range> findAllRangesThatContainKey(final Key... keys) {
    if (keys == null || keys.length == 0) {
      return new ArrayList<Range>(generation.ranges);
    }
    List<Range> list = new ArrayList<Range>(generation.ranges.size());
    OUTER: for (Range range : generation.ranges) {
      for (Key key : keys) {
        if (key == null || range.contains(key)) {
          list.add(range);
          continue OUTER;
        }
      }
    }
    return list;
  }

  private void write(FileSystem fileSystem, Path generationPath, String sessionId, Range range) throws IOException {
    //sessionId随即生成，为存索引文件的名字，写range
    FSDataOutputStream outputStream = fileSystem.create(new Path(generationPath, sessionId));
    generation.write(outputStream, range);
    outputStream.close();
  }

  private void addRanges(FileStatus status, FileSystem fileSystem) throws IOException {
    Path path = status.getPath();
    generation.readFields(fileSystem.open(path));
  }

  public String getRandomRangeName() {
    return UUID.randomUUID() + ".range";
  }
}
