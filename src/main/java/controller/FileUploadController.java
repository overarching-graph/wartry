package controller;

import java.io.*;

import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hp.file_upload.FrameworkComponents;
import com.hp.file_upload.Users;


@Controller
public class FileUploadController {
//makes directory to store uploaded files
public static String uploadDirectory = System.getProperty("user.dir")+"\\uploads";
//List of final merged components

  StringBuilder fileNames;
  List<String> namelist;
  @RequestMapping("/")
  public String UploadPage(Model model) {
 
 return "u";
  }
 
  @GetMapping("/upload")
  public String showForm(Model model,@RequestParam("files") MultipartFile[] files) {
 
      InputText ipt =new InputText();// takes input from precedence provided by user
      model.addAttribute("ipt", ipt);
      model.addAttribute("files",files);
      
       
      return "index";
}
@PostMapping("/upload")
public String submitForm(Model model,@ModelAttribute("ipt") InputText ipt,@ModelAttribute("files") MultipartFile[] files) {

//String fileNamesString=fileNames.toString();

	
	System.out.println(ipt);
  model.addAttribute("ipt", ipt);
  System.out.println(files);
  model.addAttribute("files",files);
 
 
  fileNames = new StringBuilder();
  Path fileNameAndPath = null;
  for (MultipartFile file : files) {
 fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
 fileNames.append(file.getOriginalFilename()+" ");
 
 try {
Files.write(fileNameAndPath, file.getBytes());
} catch (IOException e) {
e.printStackTrace();
}
  }
 System.out.println("check get");

 return "index";
}

String ipText;
@RequestMapping("/choice")
public String choicegraph(Model model,@ModelAttribute("ipt") InputText ipt) {
	ipText=ipt.toString();
	 System.out.println(ipt);
	 model.addAttribute("ipt", ipt);
	
	 return "choice";
}

@RequestMapping("/WithCompression")
public String compressiongraph(Model model) {
	 List<FrameworkComponents> newlist=new LinkedList<FrameworkComponents>();
	 List<FrameworkComponents> checkmerge=new LinkedList<FrameworkComponents>();
	 //List<FrameworkComponents> checkmerge2=new LinkedList<FrameworkComponents>();
	 //splitting precedence list into individual file names
	 String Names=ipText;
	 System.out.println(Names);
	 String[] namesarray = Names.split(" ");
	 Users u[] = new Users[namesarray.length];
	 System.out.println(namesarray.length);
	 
	 namelist=new LinkedList<String>();
	 ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	 try {
	 System.out.println("HIII try");

	 for(int i =0; i<namesarray.length;i++)  // checking files
	 {

	 System.out.println("HIII for");
	 u[i]= new Users();
	 System.out.println("HIII  user");

	 u[i]= mapper.readValue(new File(uploadDirectory+"\\" + namesarray[i]), Users.class);
	 }

	 String checkoutput=null,checkinput=null;
	 for(int i =0; i<namesarray.length;i++)  // checking files after reading
	 {
	 int length=u[i].getComponents().size();

	 
	 for(FrameworkComponents j:u[i].getComponents())   //iterating through components
	 {
	 if(j.getComponentType().equals("outputWriter")) {
	 checkoutput=j.getComponentName();

	 }
	 }
	 for(int klm=i+1;klm<namesarray.length;klm++)
	 {
	 for(FrameworkComponents k:u[klm].getComponents())  //iterating through components
	 {
	 if(k.getComponentType().equals("inputLoader")) {
	 checkinput=k.getComponentName();
	 
	 }
	 
	 
	 }
	 
	 //checking if files can be merged
	 if(checkoutput.equals(checkinput))
	 {
		
		 
	 if(newlist.size()==0) {
	 for(int k=0;k<length;k++)
	 {
	 newlist.add((u[i].getComponents().get(k)));
	 namelist.add(u[i].getComponents().get(k).getComponentName());
	 }
	 }
	else {
	 for(FrameworkComponents j:u[i].getComponents())  //iterating through components
	 {
	 if(!namelist.contains(j.getComponentName()))
	 {
	 for(int k=0;k<length;k++)
	 {
	 newlist.add((u[i].getComponents().get(k)));
	 namelist.add(u[i].getComponents().get(k).getComponentName());
	 }
	 }
	 }
	 }

	 
	 for(int k=1;k<u[klm].getComponents().size();k++)
	 {
	 newlist.add((u[klm].getComponents().get(k)));
	 namelist.add(u[klm].getComponents().get(k).getComponentName());

	 }
	 }
	 else
	 {
	 System.out.println(namesarray[i]+"doesnt match with"+namesarray[i+1]);
	 }
	 }
	 System.out.println("HIII inside for");

	 
	 }
	 
	 for(int i=0;i<newlist.size();i++)//printing final
	 {
	 System.out.println(newlist.get(i));
	 }
	 
	 System.out.println("---------");
	 
int l1=newlist.size();
//checkmerge=newlist;
List<String> newnamelist=new LinkedList<String>();
	 for(int i=0;i<l1-2;i++)//printing final
	 {	
		 System.out.print("checking new list");
		 System.out.println(newlist.get(i));
		 if(!newlist.get(i).getComponentType().equals("outputWriter"))
		 {
			 checkmerge.add(newlist.get(i));
			 newnamelist.add(newlist.get(i).getComponentName());
		 }
	 
	 }
	 checkmerge.add(newlist.get(l1-1));
	newnamelist.add(newlist.get(l1-1).getComponentName());
	namelist=newnamelist;
	 for(int i=0;i<checkmerge.size();i++)
	 {
		 System.out.println(checkmerge.get(i));
	 }


	 System.out.println("HIII");
	}
	 catch(Exception e)
	 {
	 e.printStackTrace();
	 }
	 Users newuser=new Users(checkmerge);
	//
	 File f=new File("C:\\Users\\Administrator\\Desktop\\Sample Project\\mergedfile.yml");
	 try {
		 	mapper.writeValue(f, newuser);
	 } catch (JsonGenerationException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 } catch (JsonMappingException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 } catch (IOException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 }
	 String str=" ";
	 //List<String> finallist=new LinkedList<String>();
	 String finallist="";
	 try {
	        BufferedReader in = new BufferedReader(new FileReader(f));
	     
	         while ((str = in.readLine()) != null) {
	            System.out.println(str);
	            finallist=finallist+"\n"+str;
	         }
	         
	         in.close();
	       
	      } catch (IOException e) {
	      }
	 f.delete();
	 //newlist.clear();
	 System.out.println(finallist);
	 model.addAttribute("listdisplay",finallist);
	 
	 return "visualize";
	 
}

@RequestMapping("/WithoutCompression")
public String withoutCompressionraph(Model model) {
		 List<FrameworkComponents> newlist=new LinkedList<FrameworkComponents>();
		 //splitting precedence list into individual file names
		 String Names=ipText;
		 System.out.println(Names);
		 String[] namesarray = Names.split(" ");
		 Users u[] = new Users[namesarray.length];
		 System.out.println(namesarray.length);
		 
		 namelist=new LinkedList<String>();
		 ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		 try {
		 System.out.println("HIII try");

		 for(int i =0; i<namesarray.length;i++)  // checking files
		 {

		 System.out.println("HIII for");
		 u[i]= new Users();
		 System.out.println("HIII  user");

		 u[i]= mapper.readValue(new File(uploadDirectory+"\\" + namesarray[i]), Users.class);
		 }

		 String checkoutput=null,checkinput=null;
		 for(int i =0; i<namesarray.length;i++)  // checking files after reading
		 {
		 int length=u[i].getComponents().size();

		 
		 for(FrameworkComponents j:u[i].getComponents())   //iterating through components
		 {
		 if(j.getComponentType().equals("outputWriter")) {
		 checkoutput=j.getComponentName();

		 }
		 }
		 for(int klm=i+1;klm<namesarray.length;klm++)
		 {
		 for(FrameworkComponents k:u[klm].getComponents())  //iterating through components
		 {
		 if(k.getComponentType().equals("inputLoader")) {
		 checkinput=k.getComponentName();

		 }
		 
		 
		 }
		 
		 //checking if files can be merged
		 if(checkoutput.equals(checkinput))
		 {
		 if(newlist.size()==0) {
		 for(int k=0;k<length;k++)
		 {
		 newlist.add((u[i].getComponents().get(k)));
		 namelist.add(u[i].getComponents().get(k).getComponentName());
		 }
		 }
		 else {
		 for(FrameworkComponents j:u[i].getComponents())  //iterating through components
		 {
		 if(!namelist.contains(j.getComponentName()))
		 {
		 for(int k=0;k<length;k++)
		 {
		 newlist.add((u[i].getComponents().get(k)));
		 namelist.add(u[i].getComponents().get(k).getComponentName());
		 }
		 }
		 }
		 }

		 
		 for(int k=1;k<u[klm].getComponents().size();k++)
		 {
		 newlist.add((u[klm].getComponents().get(k)));
		 namelist.add(u[klm].getComponents().get(k).getComponentName());

		 }
		 }
		 else
		 {
		 System.out.println(namesarray[i]+"doesnt match with"+namesarray[i+1]);
		 }
		 }
		 System.out.println("HIII inside for");

		 
		 }

		 for(int i=0;i<newlist.size();i++)//printing final
		 {
		 System.out.println(newlist.get(i));
		 }

		 System.out.println("HIII");
		}
		 catch(Exception e)
		 {
		 e.printStackTrace();
		 }
		 Users newuser=new Users(newlist);
		//
		 File f=new File("C:\\Users\\Administrator\\Desktop\\Sample Project\\mergedfile.yml");
		 try {
			 	mapper.writeValue(f, newuser);
		 } catch (JsonGenerationException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 } catch (JsonMappingException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 String str=" ";
		 //List<String> finallist=new LinkedList<String>();
		 String finallist="";
		 try {
		        BufferedReader in = new BufferedReader(new FileReader(f));
		     
		         while ((str = in.readLine()) != null) {
		            System.out.println(str);
		            finallist=finallist+"\n"+str;
		         }
		         
		         in.close();
		       
		      } catch (IOException e) {
		      }
		 f.delete();
		 //newlist.clear();
		 System.out.println(finallist);
		 model.addAttribute("listdisplay",finallist);

		 return "visualize";
		  }
 
  @RequestMapping("/visualize1")
  public String visualize1graph(Model model) {
 
 System.out.println(namelist);
 
 StringBuilder slist=new StringBuilder(namelist.toString());
 slist.deleteCharAt(0);
 slist.deleteCharAt((slist.length())-1);
 
 model.addAttribute("nodenames",slist.toString());
 
return "visualize1";
 
  }

 
}