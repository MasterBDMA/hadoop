package bdma.labos.hadoop.writer;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;

import wineinfo.avro.WineInfo;

import org.apache.hadoop.io.Text;

public class MyHDFSSequenceFileWriter implements MyWriter {
	
	private Configuration config;
	private FileSystem fs;
	private SequenceFile.Writer writer;
	
	private StringBuilder keyBuffer;
	private StringBuilder valueBuffer;
	
	public MyHDFSSequenceFileWriter() throws IOException {
		this.config = new Configuration();
		this.writer = null;
		this.reset();
	}

	public void open(String file) throws IOException {
		this.config = new Configuration();
		this.fs = FileSystem.get(config);
		Path path = new Path(file);
		if (this.fs.exists(path)) {
			System.out.println("File "+file+" already exists!");
			System.exit(1);
		}
		SequenceFile.Writer.Option[] options = new SequenceFile.Writer.Option[]
		{
				SequenceFile.Writer.file(path),
				SequenceFile.Writer.keyClass(Text.class),
				SequenceFile.Writer.valueClass(Text.class),
				SequenceFile.Writer.compression(CompressionType.NONE)
//				SequenceFile.Writer.compression(CompressionType.RECORD)
//				SequenceFile.Writer.compression(CompressionType.BLOCK)
		};
		this.writer = SequenceFile.createWriter(this.config, options);
	}
	
	public void put(WineInfo w) {
		this.keyBuffer.append(w.getType());
		this.valueBuffer.append(w.getRegion()+","+w.getAlc()+","+w.getMAcid()+","+w.getAsh()+","+w.getAlcAsh()+
				","+w.getMgn()+","+w.getTPhenols()+","+w.getFlav()+","+w.getNonflavPhenols()+","+w.getProant()+","+
				w.getCol()+","+w.getHue()+","+w.getOd280od315()+","+w.getProline());
	}
	
	public void reset() {
		this.keyBuffer = new StringBuilder();
		this.valueBuffer = new StringBuilder();
	}
	
	public int flush() throws IOException {
		String key = this.keyBuffer.toString();
		String value = this.valueBuffer.toString();
		this.writer.append(new Text(key.toString()), new Text(value.toString()));
		this.reset();
		return value.length();
	}
	
	public void close() throws IOException {
		this.writer.close();
		this.fs.close();
	}
	
}
