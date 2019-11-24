package Assignment1;

import java.io.*;
import java.util.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import analysis.*;

/**
 * Created by Hakeem on 06/11/2019.
 */

public class ProgramDependenceGraphTest {

	/**
	 * This is a sample test to check the results of your implementation. You can
	 * modify it to suit your taste
	 */
	Graph graph;
	ProgramDependenceGraph pdg;

	public ProgramDependenceGraphTest(String classPath) {
		try {
			File folder = new File(classPath);
			
			FileInputStream in;
			ClassReader classReader;
			ClassNode cn;
			
			for (File f : folder.listFiles()) {
				cn = new ClassNode(Opcodes.ASM4);
				in = new FileInputStream(f.getPath());
				classReader = new ClassReader(in);
				classReader.accept(cn, 0);
				
				for (MethodNode mn : (List<MethodNode>) cn.methods) {
					pdg = new ProgramDependenceGraph(cn, mn);
					graph = pdg.computeResult();
					Set<Node> nodeList = graph.getNodes();
					List<Node> list = new ArrayList<Node>(nodeList); 
					Node n = list.get(list.size()/2); // For example, let us get the backward slice of the middle Node
					System.out.println(cn.name + ", " + mn.name + ", " + testTightness() + ", " + testOverlap() + ", " + testBackwardSlice(n));
					//System.out.println(graph); // this prints the digraph
				}
				in.close();
			}
			System.out.println("Done");
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Set<Node> testBackwardSlice(Node n) {
		return pdg.backwardSlice(n);
	}

	private double testTightness() {
		return pdg.computeTightness();
	}

	private double testOverlap() {
		return pdg.computeOverlap();
	}

	public static void main(String[] args) throws IOException {
		// Set the path to point to the class you want to test
		String path = "../../assignment-1-lovedzx/bin/net/sf/freecol/tools";
		
		//print the header to the CSV file
		System.out.println("ClassName, MethodName, Tighness, Overlap, BackWardSlice");
		
		new ProgramDependenceGraphTest(path);
	}

}
