package analysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.multicda.cpa.CDAOptions;
import org.eclipse.emf.henshin.multicda.cpa.CpaByAGG;
import org.eclipse.emf.henshin.multicda.cpa.UnsupportedRuleException;
import org.eclipse.emf.henshin.multicda.cpa.result.CPAResult;

public class ConsSustainingAnalysis {

	static void printParIndependent(Rule rule1, Rule rule2) {
		System.out.print(" " + (checkParIndependent(rule1, rule2) ? "+" : "-"));
	}

	static void printSeqIndependent(Rule rule1, Rule rule2) {
		System.out.print(" " + (checkSeqIndependent(rule1, rule2) ? "+" : "-"));
	}

	static void printParDependent(Rule rule1, Rule rule2) {
		System.out.print(" " + (checkParDependent(rule1, rule2) ? "+" : "-"));
	}

	static void printSeqDependent(Rule rule1, Rule rule2) {
		System.out.print(" " + (checkSeqDependent(rule1, rule2) ? "+" : "-"));
	}
	
	public static CPAResult performAnalysis(Set<Rule> domainRules, Set<Rule> checkRules, boolean conflicts) {
		CpaByAGG cpa = new CpaByAGG();
		CDAOptions options = new CDAOptions();

		try {
			cpa.init(domainRules, checkRules, options );
		} catch (UnsupportedRuleException e) {
			e.printStackTrace();
		}
		PrintStream original = System.out;
		System.setOut(new NullPrintStream());
		CPAResult result = null;
		if (conflicts)
			result = cpa.runConflictAnalysis();
		else
			result = cpa.runDependencyAnalysis();
			
		System.setOut(original);
		return result;
	}
	

	private static CPAResult performAnalysis(Rule rule1, Rule rule2, boolean conflicts) {
		CpaByAGG cpa = new CpaByAGG();
		CDAOptions options = new CDAOptions();

		try {
			cpa.init(Collections.singleton(rule1), Collections.singleton(rule2), options );
		} catch (UnsupportedRuleException e) {
			e.printStackTrace();
		}
		PrintStream original = System.out;
		System.setOut(new NullPrintStream());
		CPAResult result = null;
		if (conflicts)
			result = cpa.runConflictAnalysis();
		else
			result = cpa.runDependencyAnalysis();
			
		System.setOut(original);
		return result;
	}


	static boolean checkParDependent(Rule rule1, Rule rule2) {
		CPAResult result = performAnalysis(rule1, rule2, true);
		return !result.getCriticalPairs().isEmpty();
	}

	static boolean checkParIndependent(Rule rule1, Rule rule2) {
		CPAResult result = performAnalysis(rule1, rule2, true);
		return result.getCriticalPairs().isEmpty();
	}

	static boolean checkSeqIndependent(Rule rule1, Rule rule2) {
		CPAResult result = performAnalysis(rule1, rule2, false);
		return result.getCriticalPairs().isEmpty();
	}
	
	static boolean checkSeqDependent(Rule rule1, Rule rule2) {
		CPAResult result = performAnalysis(rule1, rule2, false);
		return !result.getCriticalPairs().isEmpty();
	}

	public static void printConsistencySustaining(Rule domainRule, Rule checkPC) {
		System.out.print(" " + (checkConsistencySustaining(domainRule, checkPC) ? "+" : "?"));
	}

	public static void printConsistencySustaining(Rule domainRule, Rule checkPC, Rule checkPCPrime) {
		System.out.print(" " + (checkConsistencySustaining(domainRule, checkPC, checkPCPrime) ? "+" : "?"));

	}

	public static void printConsistencyImproving(Rule domainRule, Rule checkPC) {
		System.out.print(" " + (checkConsistencyImproving(domainRule, checkPC) ? "?" : "-"));

	}

	public static void printConsistencyImproving(Rule domainRule, Rule checkPC, Rule checkPCPrime) {
		System.out.print(" " + (checkConsistencyImproving(domainRule, checkPC, checkPCPrime) ? "?" : "-"));

	}

	private static boolean checkConsistencySustaining(Rule domainRule, Rule checkPC) {
		return checkSeqIndependent(domainRule, checkPC);
	}

	private static boolean checkConsistencySustaining(Rule domainRule, Rule checkPC, Rule checkPCPrime) {
		return checkSeqIndependent(domainRule, checkPC) && checkParIndependent(domainRule, checkPCPrime);
	}

	private static boolean checkConsistencyImproving(Rule domainRule, Rule checkPC) {
		return checkParDependent(domainRule, checkPC);
	}

	private static boolean checkConsistencyImproving(Rule domainRule, Rule checkPC, Rule checkPCPrime) {
		return checkParDependent(domainRule, checkPC) || checkSeqDependent(domainRule, checkPCPrime);

	}
}

class NullPrintStream extends PrintStream {

	  public NullPrintStream() {
	    super(new NullByteArrayOutputStream());
	  }

	  private static class NullByteArrayOutputStream extends ByteArrayOutputStream {

	    @Override
	    public void write(int b) {
	      // do nothing
	    }

	    @Override
	    public void write(byte[] b, int off, int len) {
	      // do nothing
	    }

	    @Override
	    public void writeTo(OutputStream out) throws IOException {
	      // do nothing
	    }

	  }

	}
