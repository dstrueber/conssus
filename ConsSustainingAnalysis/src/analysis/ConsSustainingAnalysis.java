package analysis;

import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.multicda.cda.ConflictAnalysis;
import org.eclipse.emf.henshin.multicda.cda.DependencyAnalysis;
import org.eclipse.emf.henshin.multicda.cda.units.Atom.ConflictAtom;
import org.eclipse.emf.henshin.multicda.cda.units.Atom.DependencyAtom;

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

	static boolean checkParIndependent(Rule rule1, Rule rule2) {
		ConflictAnalysis a = new ConflictAnalysis(rule1, rule2);
		ConflictAtom atoms = a.computeResultsBinary();
		return atoms == null;
	}

	static boolean checkSeqIndependent(Rule rule1, Rule rule2) {
		DependencyAnalysis a = new DependencyAnalysis(rule1, rule2);
		DependencyAtom atoms = a.computeResultsBinary();
		return atoms == null;
	}

	static boolean checkParDependent(Rule rule1, Rule rule2) {
		ConflictAnalysis a = new ConflictAnalysis(rule1, rule2);
		ConflictAtom atoms = a.computeResultsBinary();
		return atoms != null;
	}

	static boolean checkSeqDependent(Rule rule1, Rule rule2) {
		DependencyAnalysis a = new DependencyAnalysis(rule1, rule2);
		DependencyAtom atoms = a.computeResultsBinary();
		return atoms != null;
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
