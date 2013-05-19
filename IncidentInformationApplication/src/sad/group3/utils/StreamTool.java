package sad.group3.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
	/**
	 * 读取流中的数据
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] read(InputStream is) throws Exception{
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		while((len=is.read(buffer))!=-1){
			outputStream.write(buffer,0,len);
		}
		is.close();
		return outputStream.toByteArray();
	}

}
