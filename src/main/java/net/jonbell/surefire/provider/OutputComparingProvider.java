package net.jonbell.surefire.provider;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.maven.surefire.junitcore.JUnitCoreProvider;
import org.apache.maven.surefire.providerapi.AbstractProvider;
import org.apache.maven.surefire.providerapi.ProviderParameters;
import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.testset.TestSetFailedException;

public class OutputComparingProvider extends AbstractProvider {
	private AbstractProvider[] providers;
	private JUnitCoreProvider baseProvider;

	public OutputComparingProvider(ProviderParameters booterParameters) {
		baseProvider = new JUnitCoreProvider(booterParameters);
		String cps = booterParameters.getProviderProperties().get("cp");
		String[] cp = cps.split(",");
		providers = new AbstractProvider[cp.length];
		for (int i = 0; i < cp.length; i++) {
			cp[i] = cp[i].trim();
		}
	}

	public void instrument() {
		// Iterable<Class<?>> iter = getSuites();
		try {
			String jarPath = "/Users/Ying/Study/Columbia/Program/6901MutableReplay/chroniclerj/Code/ChroniclerJ/target/ChroniclerJ-0.42-SNAPSHOT.jar";
			String testCasePath = "/Users/Ying/Study/Columbia/Program/6901MutableReplay/chroniclerj/Code/ChroniclerJ/target/test-classes/edu/columbia/cs/psl/test/chroniclerj/RandomITCase.class";
			System.out.println("=======Chronicler J Instrument start========");
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, "-instrument", testCasePath, "target/deploy",
					"target/replay");
			// pb.directory(new File("preferred/working/directory"));
			Process p = pb.start();
			p.waitFor();
		} catch (Exception e) {
			System.out.println("============Instrument Exception===========");
			e.printStackTrace();
		}
	}

	public void generate() {
		try {
			String jarPath = "/Users/Ying/Study/Columbia/Program/6901MutableReplay/chroniclerj/Code/ChroniclerJ/target/ChroniclerJ-0.42-SNAPSHOT.jar";
			String testCasePath = "/Users/Ying/Study/Columbia/Program/6901MutableReplay/chroniclerj/Code/ChroniclerJ/target/depoly/RandomITCase.class";
			System.out.println("=======Chronicler J Generate start========");
			ProcessBuilder pb = new ProcessBuilder("java", jarPath, testCasePath);
			// pb.directory(new File("preferred/working/directory"));
			Process p = pb.start();
			p.waitFor();
		} catch (Exception e) {
			System.out.println("============Generate Exception===========");
			e.printStackTrace();
		}
	}

	public Iterable<Class<?>> getSuites() {
		Iterable<Class<?>> ret = baseProvider.getSuites();
		return ret;
	}

	public RunResult invoke(Object forkTestSet)
			throws TestSetFailedException, ReporterException, InvocationTargetException {
		instrument();
		generate();
		return baseProvider.invoke(forkTestSet);
	}

}
