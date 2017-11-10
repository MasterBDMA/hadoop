package bdma.labos.hadoop.writer;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;

import wineinfo.avro.WineInfo;

import org.apache.hadoop.io.Text;

public class MyAvroFileWriter implements MyWriter {
	
	private Configuration config;
	private FileSystem fs;
	
	DataFileWriter<WineInfo> dataFileWriter;
	
	public MyAvroFileWriter() throws IOException {
		this.config = new Configuration();
		dataFileWriter = null;
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
		
		DatumWriter<WineInfo> wineInfoDatumWriter = new SpecificDatumWriter<WineInfo>(WineInfo.class);
		dataFileWriter = new DataFileWriter<WineInfo>(wineInfoDatumWriter);
		dataFileWriter.create(WineInfo.getClassSchema(), new File(file));
	}
	
	public void put(WineInfo w) {
		try {
			this.dataFileWriter.append(w);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reset() {
		
	}
	
	
	public int flush() throws IOException {
		this.dataFileWriter.flush();
		return 1;
	}
	
	public void close() throws IOException {
		this.dataFileWriter.close();
	}
	
}
