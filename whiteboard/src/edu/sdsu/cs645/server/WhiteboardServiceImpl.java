package edu.sdsu.cs645.server;

import edu.sdsu.cs645.client.WhiteboardService;
import edu.sdsu.cs645.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.*;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WhiteboardServiceImpl extends RemoteServiceServlet implements
    WhiteboardService {
		
		public String validateLogin(String s){
			if(s.equals("sp2018")) return "OK";
			return "INVALID";			
		}		
		
			public String save(String contents,String username){
			String path =  getServletContext().getRealPath("/");
			String filename = path + "/data.txt";
			try{
				PrintWriter out = new PrintWriter(
					new FileWriter(filename));
					if(contents.equals("")){
						out.print(contents);
					}else{
						contents = contents.replace(username+':',"");
						out.print(username + "#" + contents);
					}					
					out.close();					
			}catch(Exception e){
				return "ERROR" +e;
			}
			return "OK";
		}	
		
		public String load(){
			String path =  getServletContext().getRealPath("/");
			String filename = path + "/data.txt";
			StringBuffer contents = new StringBuffer();
			String line;
			try{
				BufferedReader in = new BufferedReader(
				new FileReader(filename));
				while((line= in.readLine())!=null)
				{
					String[] text = line.split("#");
					contents.append(text[0] + ": ");
					contents.append(text[1]);
				}
					//contents.append(line);
				in.close();					
			}catch(Exception e){
				return "ERROR" +e;
			}
			return contents.toString();
		}
}