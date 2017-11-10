package bdma.labos.hadoop.writer;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;

import wineinfo.avro.WineInfo;

import org.apache.hadoop.io.Text;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.avro.AvroSchemaConverter;
import org.apache.parquet.avro.AvroWriteSupport;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;

public class MyParquetWriter implements MyWriter {
	
	private Configuration config;
	private FileSystem fs;
	
	AvroParquetWriter parquetWriter;
	
	public MyParquetWriter() throws IOException {
		this.config = new Configuration();
		parquetWriter = null;
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
		parquetWriter = new AvroParquetWriter<GenericRecord>(path,WineInfo.SCHEMA$,
				CompressionCodecName.UNCOMPRESSED,
		          ParquetWriter.DEFAULT_BLOCK_SIZE,
		          ParquetWriter.DEFAULT_PAGE_SIZE,
		          true);
	}
	
	public void put(WineInfo w) {
		try {
			this.parquetWriter.write(w);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reset() {
		
	}
	
	
	public int flush() throws IOException {
		return 1;
	}
	
	public void close() throws IOException {
		this.parquetWriter.close();
	}
	
}
