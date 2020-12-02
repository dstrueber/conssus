package analysis;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

public class AnalysisDriver {
	final static String INPUT_RULE_PATH = "cases/cragenerated";

	private static Module defineInputRules(HenshinResourceSet resourceSet) {
		if (INPUT_RULE_PATH.equals("cases/cramanual")) {
			Module domainRules = resourceSet.getModule("mutation.henshin");
			return domainRules;
		}
		if (INPUT_RULE_PATH.equals("cases/cragenerated")) {
			Module module = HenshinPackage.eINSTANCE.getHenshinFactory().createModule();
			for (File file : new File(INPUT_RULE_PATH).listFiles()) {
				if (file.getName().endsWith(".henshin") &&
						!file.getName().equals("checkrules.henshin")) {
					Module m = resourceSet.getModule(file.getName());
					module.getUnits().addAll(m.getUnits());
				}
			}
			System.out.println(module.getUnits().size());
			return module;

		}
		return null;
	}

	public static void main(String[] args) {
		disableErr(); // to avoid awkward formatting issue
		HenshinResourceSet resourceSet = new HenshinResourceSet(INPUT_RULE_PATH);

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		Module checkRuleModule = resourceSet.getModule("checkrules.henshin");
		Rule checkPC1 = (Rule) checkRuleModule.getUnit("checkPC1");
		Rule checkPC2 = (Rule) checkRuleModule.getUnit("checkPC2");
		Rule checkPC2Prime = (Rule) checkRuleModule.getUnit("checkPC2Prime");
		Rule checkPC3 = (Rule) checkRuleModule.getUnit("checkPC3");
		Rule checkPC3Prime = (Rule) checkRuleModule.getUnit("checkPC3Prime");
		List<Rule> checkRules = new ArrayList<>();
		checkRules.add(checkPC1);
		checkRules.add(checkPC2);
		checkRules.add(checkPC2Prime);
		checkRules.add(checkPC3);
		checkRules.add(checkPC3Prime);

		Module domainRules = defineInputRules(resourceSet);

		for (Rule domainRule : domainRules.getAllRules()) {
			System.out.print(domainRule.getName() + " \t");
			domainRule.getLhs().setFormula(null);

			ConsSustainingAnalysis.printSeqIndependent(domainRule, checkPC1);
			ConsSustainingAnalysis.printSeqIndependent(domainRule, checkPC2);
			ConsSustainingAnalysis.printSeqIndependent(domainRule, checkPC3);
			ConsSustainingAnalysis.printParIndependent(domainRule, checkPC2Prime);
			ConsSustainingAnalysis.printParIndependent(domainRule, checkPC3Prime);

			ConsSustainingAnalysis.printParDependent(domainRule, checkPC1);
			ConsSustainingAnalysis.printParDependent(domainRule, checkPC2);
			ConsSustainingAnalysis.printParDependent(domainRule, checkPC3);
			ConsSustainingAnalysis.printSeqDependent(domainRule, checkPC2Prime);
			ConsSustainingAnalysis.printSeqDependent(domainRule, checkPC3Prime);

			System.out.print("\t");
			ConsSustainingAnalysis.printConsistencySustaining(domainRule, checkPC1);
			ConsSustainingAnalysis.printConsistencySustaining(domainRule, checkPC2, checkPC2Prime);
			ConsSustainingAnalysis.printConsistencySustaining(domainRule, checkPC3, checkPC3Prime);

			System.out.print("\t");
			ConsSustainingAnalysis.printConsistencyImproving(domainRule, checkPC1);
			ConsSustainingAnalysis.printConsistencyImproving(domainRule, checkPC2, checkPC2Prime);
			ConsSustainingAnalysis.printConsistencyImproving(domainRule, checkPC3, checkPC3Prime);
			System.out.println();
		}
	}

	private static void disableErr() {
		System.setErr(new PrintStream(new OutputStream() {
			public void write(int b) {
			}
		}));

	}

}
