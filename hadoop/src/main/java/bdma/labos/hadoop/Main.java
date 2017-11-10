package bdma.labos.hadoop;

// Its my file: esunen

import java.io.IOException;

import bdma.labos.hadoop.reader.MyHDFSSequenceFileReader;
import bdma.labos.hadoop.reader.MyReader;
import bdma.labos.hadoop.writer.MyAvroFileWriter;
import bdma.labos.hadoop.writer.MyHDFSPlainFileWriter;
import bdma.labos.hadoop.writer.MyHDFSSequenceFileWriter;
import bdma.labos.hadoop.writer.MyParquetWriter;
import bdma.labos.hadoop.writer.MyWriter;
import wineinfo.avro.WineInfo;
import wineinfo.data_model.Generator;

public class Main {
		
	private static MyReader input;
	private static MyWriter output;
	private static String file;
	
	public static void read() throws IOException {
		input.open(file);
		String line = input.next();
		while (line != null) {
			if (!line.equals("")) {
				System.out.println(line);
			}
			line = input.next();
		}
		input.close();
	}
	
	public static void write(long number) throws IOException {
		output.open(file);
		for (int inst = 0; inst < number; ++inst) {
			WineInfo w = Generator.generateNewInstance(System.currentTimeMillis());
			output.put(w);
			output.flush();
		}
		output.close();
	}

	public static void main(String[] args) {
		try {
			if (args[0].equals("write")) {
				//Possible formats
				if (args[1].equals("-plainText")) {
					output = new MyHDFSPlainFileWriter();
					file = args[3];
				}
				else if (args[1].equals("-sequenceFile")) {
					output = new MyHDFSSequenceFileWriter();
					file = args[3];
				}
				else if (args[1].equals("-avro")) {
					output = new MyAvroFileWriter();
					file = args[3];
				}
				else if (args[1].equals("-parquet")) {
					output = new MyParquetWriter();
					file = args[3];
				}
				
				write(Integer.parseInt(args[2]));
			}
			else if (args[0].equals("read")) {
				if (args[1].equals("-sequenceFile")) {
					input = new MyHDFSSequenceFileReader(); 
				}
				if (args[1].equals("-plainText")) {
					input = new MyHDFSSequenceFileReader(); 
				}
				file = args[2]; //here, file is replaced for the table name in HBase
				read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
