import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
public class Main {
    public static void main(String[] args) throws Exception{
        if(args.length==3){
            if(args[0].equals(args[1])) throw new Exception("Input file should not equal output file. Will cause input file to lose information.");
            String third_opt=args[2];
            final Boolean is_add=third_opt.equals("add");
            final Boolean is_remove=third_opt.equals("remove");
            if(!(is_add||is_remove))
                throw new Exception("Third argument should be add/remove.");
            CheckValidPathName(args[0]);
            CheckValidPathName(args[1]);
            String input_str=ParseToAbsolute(args[0]);
            String output_str=ParseToAbsolute(args[1]);
            System.out.println("Reading file \""+input_str+"\"");
            System.out.println("Outputting to file \""+output_str+"\"");
            BufferedWriter output=null;
            BufferedReader input=null;
            try{
                File file_input=new File(input_str);
                File file_output=new File(output_str);
                if(!file_input.exists()){
                    throw new Exception("Input file required. Non-existant file: \""+input_str+"\"");
                }
                if(!file_output.exists()){
                    file_output.createNewFile();
                }
                input=new BufferedReader(new FileReader(file_input));
                output=new BufferedWriter(new FileWriter(file_output));
                Vector<String> str_lines=new Vector<>();
                String read_str;
                while((read_str=input.readLine())!=null){
                    str_lines.add(read_str);
                }
                if(is_add){
                    System.out.println("Option chosen: add");
                    for(int i=0;i<str_lines.size();i++){
                        String added_chars_str="\""+str_lines.get(i)+"\\n\"";
                        output.write(added_chars_str.replaceAll("^\"\\\\n\"","")+"\n"); //To remove "\n" as it is unnecessary
                    }
                }else{
                    System.out.println("Option chosen: remove");
                    int read_lines=0;
                    for(int i=0;i<str_lines.size();i++){
                        if(!str_lines.get(i).startsWith("\"")){ //substring will remove first character regardles if it's quote or not.
                            output.write("\n"); //Just write newline.
                            read_lines++;
                            continue;
                        } 
                        int adjust_sub_i=str_lines.get(i).length()!=0?1:0; //No index out of bounds error if "\n" was removed.
                        String removed_chars_str=str_lines.get(i).substring(adjust_sub_i,str_lines.get(i).length()).replace("\\n\"","");
                        output.write(removed_chars_str+"\n");
                    }
                    if(read_lines==str_lines.size()) throw new Exception("No output in output file because there were no (\") or (\\n) to remove.");
                }
            }catch(IOException e){
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }finally{
                if(output!=null) output.close();
                if(input!=null) input.close();
            }
        }else{
            System.out.println("Help: Converts each line in a GLSL file to add \"quotes\" and \"\\n\" in between.\n"
            +"The purpose or programming this is to write GLSL into a (constexpr) const char* instead of external .vert/.frag files.\n"
            +"Args: 2 File names (input_file output_file) without backslashes (Add quotes if spaces are included).\n"
            +"Third argument is to add or remove the quotes from the input file to the output file. Use add/remove for the third argument.\n"
            +"Example: java -jar glsl_char_conv.jar vertex.vert output.txt add");
        }
    }
    /**
     * <p>Output 2 values: Outputs absolute file path.
     * @param file_name Relative file name with '../' pattern appended at beginning.
     */ 
    public static String ParseToAbsolute(String file_name){
        String file_name_temp=file_name; //To compare file name and change cwd when ../ is used.
        String cwd=System.getProperty("user.dir").replaceAll("\\\\","/");
        String go_back_pattern = "^[.][.]/"; //Dots represented as dots only and not capture all.
        file_name_temp=file_name;
        file_name=file_name.replaceFirst(go_back_pattern,"");
        do{
            if(file_name.length()==file_name_temp.length())
                break;
            cwd=cwd.replaceFirst("/[[\\w_ ]*[.]*]+$",""); //Ex: 'f1/f2/weird_dir_with.dot and_space' to 'f1/f2'
            file_name_temp=file_name;
            file_name=file_name.replaceFirst(go_back_pattern,"");
        }while(file_name.length()!=file_name_temp.length());
        return cwd+"/"+file_name;
    }
    /**
     * <p>Throws Exception if file_name is invalid with this program.
     * @param file_name File name to check.
     */ 
    public static void CheckValidPathName(String file_name) throws Exception{
        String pattern = "^([.]?/?)*([\\w_/ ]+.*[\\w_ ]+)+$";
        if(!file_name.matches(pattern)){
            throw new Exception("Unable to parse as a valid file name: \""+file_name+"\"");
        }
    }
}
