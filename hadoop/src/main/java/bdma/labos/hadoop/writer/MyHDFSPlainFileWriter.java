package bdma.labos.hadoop.writer;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import wineinfo.avro.WineInfo;

public class MyHDFSPlainFileWriter implements MyWriter {

	private Configuration config;
	private FileSystem fs;
	private FSDataOutputStream output;
	
	private StringBuilder buffer;
	private boolean first;
	
	public MyHDFSPlainFileWriter() {
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
		this.output = this.fs.create(path);
	}
	
	
	public void put(WineInfo w) {
		this.buffer.append(w.getType()+","+w.getRegion()+","+w.getAlc()+","+w.getMAcid()+","+w.getAsh()+","+w.getAlcAsh()+
				","+w.getMgn()+","+w.getTPhenols()+","+w.getFlav()+","+w.getNonflavPhenols()+","+w.getProant()+","+
				w.getCol()+","+w.getHue()+","+w.getOd280od315()+","+w.getProline());
	}
	
	public void reset() {
		this.buffer = new StringBuilder();
		this.first = true;
	}
	
	public int flush() throws IOException {
		String data = this.buffer.toString()+'\n';
		this.output.write(data.getBytes());
		this.reset();
		return data.length();
	}
	
	public void close() throws IOException {
		this.output.flush();
		this.output.close();
		this.fs.close();
	}
	
}
