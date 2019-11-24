package analysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will write a single CSV file of metrics for a class.
 *
 *
 * Created by neilwalkinshaw on 24/10/2017.
 */
public class ClassMetrics {


    /**
     * First argument is the class name, e.g. /java/awt/geom/Area.class"
     * These seond argument is the name of the target csv file, e.v. "classMetrics.csv"
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	
    	String srcpath ="/s_home/lc443/Desktop/assignment-1-lovedzx/src/net/sf/freecol/tools/";
    	 File srcfolder = new File(srcpath);
         PrintStream fileOutput = new PrintStream("/s_home/lc443/Desktop/output/loc.csv");
         System.setOut(fileOutput);
         String output = "File, Lines of Code";
         System.out.println(output);
         
         for(File file : srcfolder.listFiles()) {
         	int lines = countLines(file);
         	output = file.toString();
         	output += ","+Integer.toString(lines);
         	System.out.println(output.toString());
         }
    	
        //Read in the class given in the argument to a ClassNode.
        //ClassNode cn = new ClassNode(Opcodes.ASM4);
        //InputStream in=CFGExtractor.class.getResourceAsStream("/java/awt/geom/Area.class");
     // InputStream in=CFGExtractor.class.getResourceAsStream("../../assignment-1-lovedzx/bin/net/sf/freecol/tools/ClientInputHandler.class");
        String path = "../../assignment-1-lovedzx/bin/net/sf/freecol/tools/";
        File folder = new File(path);
        
        FileInputStream in;
      	ClassReader classReader;
      	ClassNode cn;
      		
        PrintStream fileOut = new PrintStream("/s_home/lc443/Desktop/output/CC.csv");
        System.setOut(fileOut);
        String record;
        record = "Method,CC";
        System.out.println(output);

        
        System.out.println(record);
		for (File f : folder.listFiles()) {
			cn = new ClassNode(Opcodes.ASM4);
			in = new FileInputStream(f.getPath());
			classReader = new ClassReader(in);
			classReader.accept(cn, 0);
			
        for(MethodNode mn : (List<MethodNode>)cn.methods){
            int numNodes = -1;
            int cyclomaticComplexity = -1; // both values default to -1 if they cannot be computed.
            try {
                Graph cfg = CFGExtractor.getCFG(cn.name, mn);
       
                cyclomaticComplexity = getCyclomaticComplexity(cfg);

            } catch (AnalyzerException e) {
                e.printStackTrace();
            }

            //Write the method details and metrics to the CSV record.
            record = cn.name+"."+mn.name+", "; //Add method signature in first column.
            record += Integer.toString(cyclomaticComplexity)+"\n";
            //csvPrinter.printRecord(record);
            System.out.println(record.toString());
        }
		}
        //csvPrinter.close();
    }
    
    private static int countLines(File file) throws IOException{
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	int lines = 0;
    	while(reader.readLine() != null) lines++;
    	reader.close();
    	return lines;
    }
    
    private static int getCyclomaticComplexity(Graph cfg){
        int branchCount = 0;
        for(Node n : cfg.getNodes()){
            if(cfg.getSuccessors(n).size()>1){
                branchCount ++;
            }
        }
        return branchCount + 1;
    }


}
