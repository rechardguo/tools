package rechard.dbs.tool.simulate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SimulateUFF {

	/*public static void main(String[]args){
		RandomAccessFile aFile;
		try {
			aFile = new RandomAccessFile("D:\\code\\DBS_CB\\CORP_PSO_SRC\\Actuate\\BIRT\\S1\\src\\rechard\\dbs\\tool\\simulate\\TW_UFF_01_a7.txt", "rw");
			FileChannel fileChannel = aFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(48);
			while((fileChannel.read(buf))!=-1){

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/


	public static void writeSimulateFile(String content){
		try {
			File file = new File("D://upload_file//uff//TW_UFF.txt");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String simulateContent(){
		BufferedReader br;
		FileReader is;
		StringBuffer sb = new StringBuffer();
		try {
			is = new FileReader("D:\\code\\DBS_CB\\CORP_PSO_SRC\\Actuate\\BIRT\\S1\\src\\rechard\\dbs\\tool\\simulate\\TW_UFF_01_a7.txt");
			br = new BufferedReader(is);
			String line = null;
			while((line=br.readLine())!=null){
				if(line.startsWith("PAYMENT")){
					String [] linearr= line.split("\\,",-1);
					String payment = linearr[2-1];
					if(payment.equals("TT")){
						linearr[39-1]="DO";
						linearr[43-1]="693";
						linearr[44-1]="1";
						linearr[69-1]="A";
						linearr[71-1]="210";
					}else if(payment.equals("ACT")){
						linearr[39-1]="DO";
						linearr[43-1]="695";
						linearr[44-1]="3";
						linearr[69-1]="A";
						linearr[71-1]="210";
					}
					/*System.out.println("payment："+linearr[2-1]);
					System.out.println("beneficiary code(payee role)："+linearr[43-1]);
					System.out.println("Domestic/overseas："+linearr[39-1]);
					System.out.println("purpose code："+linearr[43-1]);
					System.out.println("Specific Payment Purpose 1st level："+linearr[69-1]);
					System.out.println("Specific Payment Purpose 2nd level:"+linearr[71-1]);
					System.out.println("description"+linearr[44-1]);*/
					String newLine="";
					for (int i = 0; i < linearr.length; i++) {
						newLine+=linearr[i];
						if(i<linearr.length-1){
							newLine+=",";
						}
					}
					sb.append(newLine);
				}else{
					sb.append(line);
				}
				sb.append("\r\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}


	public static void main(String[]args){
		writeSimulateFile(simulateContent());
	}
}
