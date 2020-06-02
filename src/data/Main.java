package data;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Main {
		public static void main(String[] args) throws IOException {

			ArrayList<Peer> listPeer =new ArrayList<Peer>();
			Peer newPeer= new Peer();
			BufferedReader br =null;
			try {
				File a = new File("a.txt");
				if(!a.exists()) a.createNewFile();
				br = new BufferedReader(new FileReader("C:\\Users\\Do Xuan Tho\\Desktop\\test.txt"));
				System.out.print("Content \n");
				int num=0;
				char ch;
				int j=0;
				String temp="";
				while ((num=br.read())!=-1) {
					ch=(char)num;
					if (ch!='\n') {
						if (ch!=',') {
							temp=temp+ch;
						}else {
							switch(j) {
							case 0: newPeer.setName(temp); break;
							case 1: newPeer.setPass(temp); break;
							case 2: newPeer.setHost(temp); break;
							case 3: newPeer.setPort(Integer.parseInt(temp) ); break;
							case 4: newPeer.setState(Boolean.parseBoolean(temp)); break;
							}
							j++;
							temp="";
						}
						
								
					}else {
						switch(j) {
						case 0: newPeer.setName(temp); break;
						case 1: newPeer.setPass(temp); break;
						case 2: newPeer.setHost(temp); break;
						case 3: newPeer.setPort(Integer.parseInt(temp) ); break;
						case 4: newPeer.setState(Boolean.parseBoolean(temp)); break;
						}
						j=0;
						listPeer.add(newPeer);
						newPeer=new Peer();
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				br.close();
			}
			for (int i = 0; i < listPeer.size(); i++) {
				System.out.print(listPeer.get(i).getName());System.out.print("  ");
				System.out.print(listPeer.get(i).getPass());System.out.print("  ");
				System.out.print(listPeer.get(i).getHost());System.out.print("  ");
				System.out.print(listPeer.get(i).getPort());System.out.print("  ");
				System.out.println(listPeer.get(i).getState());
			}
		
		}
			
		public ArrayList<Peer> loadPeer() throws IOException{
			ArrayList<Peer> listPeer =new ArrayList<Peer>();
			Peer newPeer= new Peer();
			BufferedReader br =null;
			try {
				br = new BufferedReader(new FileReader("C:\\Users\\Do Xuan Tho\\Desktop\\test.txt"));
				System.out.print("Nội dung \n");
				int num=0;
				char ch;
				int j=0;
				String temp="";
				while ((num=br.read())!=-1) {
					ch=(char)num;
					if (ch!='\n') {
						if (ch!=',') {
							temp=temp+ch;
						}else {
							switch(j) {
							case 0: newPeer.setName(temp); break;
							case 1: newPeer.setPass(temp); break;
							case 2: newPeer.setHost(temp); break;
							case 3: newPeer.setPort(Integer.parseInt(temp) ); break;
							case 4: newPeer.setState(Boolean.parseBoolean(temp)); break;
							}
							j++;
							temp="";
						}
						
								
					}else {
						switch(j) {
						case 0: newPeer.setName(temp); break;
						case 1: newPeer.setPass(temp); break;
						case 2: newPeer.setHost(temp); break;
						case 3: newPeer.setPort(Integer.parseInt(temp) ); break;
						case 4: newPeer.setState(Boolean.parseBoolean(temp)); break;
						}
						j=0;
						listPeer.add(newPeer);
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				br.close();
			}
//			for (int i = 0; i < listPeer.size(); i++) {
//				System.out.print(listPeer.get(i).getName());System.out.print("  ");
//				System.out.print(listPeer.get(i).getPass());System.out.print("  ");
//				System.out.print(listPeer.get(i).getHost());System.out.print("  ");
//				System.out.print(listPeer.get(i).getPort());System.out.print("  ");
//				System.out.println(listPeer.get(i).getState());
//			}
			return listPeer;
		}
		public static void storePeer(ArrayList<Peer> listPeer) throws IOException {
			FileWriter out=null;
			Peer peer=new Peer();
			try {
				out =  new FileWriter("C:\\Users\\Do Xuan Tho\\Desktop\\test.txt");
				for (int i = 0; i < listPeer.size(); i++) {
					peer=listPeer.get(i);
					out.write(peer.getName());out.write(',');
					out.write(peer.getPass());out.write(',');
					out.write(peer.getHost());out.write(',');
					out.write(Integer.toString(peer.getPort()));out.write(',');
					out.write(Boolean.toString(peer.getState()));out.write(',');
					out.write('\n');
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				out.close();
			}
		}
}
