package rechard.dbs.tool.simulate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MergeUtil {

	static String file ="D:\\merge\\mergerlist.txt";
	static String branch_dev="D:\\code\\DBS_CB\\CORP_PSO_SRC£¨ant»áµÇÂ½²»ÁË£©\\";
	static String branch_merge="D:\\code\\DBS_CB\\CORP_PSO_SRC\\";
	static String folder_dev="D:\\merge\\branch_dev\\";
	static String folder_merge="D:\\merge\\branch_merge\\";
	public static void main(String[]args){
		//prepareChangeFile4Compare();
		mergedone();
	}
	
	
	private static void mergedone() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = reader.readLine())!=null){
				copy(folder_merge+line,branch_merge+line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void prepareChangeFile4Compare() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = reader.readLine())!=null){
				System.out.println(line);
				copy(branch_dev+line,folder_dev+line);
				copy(branch_merge+line,folder_merge+line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void copy(String srcFileName, String destFileName) {
        try {
        	File srcFile = new File(srcFileName);
        	File destFile = new File(destFileName);
        	if(!destFile.getParentFile().exists()){
        		destFile.getParentFile().mkdirs();
        	}
            FileInputStream input = new FileInputStream(srcFile);
            FileOutputStream output = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


