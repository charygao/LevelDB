package cn.edu.nju.file;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Value implements Writable {

  private static final String UTF_8 = "UTF-8";

  private BytesWritable data = new BytesWritable();

  public Value() {

  }

  public Value(byte[] data) {
    this.data.set(data, 0, data.length);
  }

  public BytesWritable getData() {
    return data;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    data.write(out);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    data.readFields(in);
  }

  public void set(Value v) {
    data.set(v.data);
  }

  @Override
  public String toString() {
    return new String(data.getBytes(), 0, data.getLength());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Value other = (Value) obj;
    if (data == null) {
      if (other.data != null)
        return false;
    } else if (!data.equals(other.data))
      return false;
    return true;
  }
}
