package de.topobyte.click.that.hood;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.ArgumentParser;
import de.topobyte.utilities.apache.commons.cli.commands.ExeRunner;
import de.topobyte.utilities.apache.commons.cli.commands.ExecutionData;
import de.topobyte.utilities.apache.commons.cli.commands.RunnerException;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.DelegateExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;
import de.topobyte.utilities.apache.commons.cli.parsing.ArgumentParseException;

public class ClickThatHoodUtils
{

	final static Logger logger = LoggerFactory
			.getLogger(ClickThatHoodUtils.class);

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			DelegateExeOptions options = new DelegateExeOptions();
			options.addCommand("check", Check.OPTIONS_FACTORY, Check.class);
			options.addCommand("fix", Fix.class);

			return options;
		}

	};

	public static void main(String[] args) throws RunnerException
	{
		ExeOptions options = OPTIONS_FACTORY.createOptions();
		ArgumentParser parser = new ArgumentParser("click-that-hood-utils",
				options);

		ExecutionData data = parser.parse(args);
		if (data != null) {
			ExeRunner.run(data);
		}
	}

	public static class Check
	{

		private static final String OPTION_DETAILS = "details";

		public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

			@Override
			public ExeOptions createOptions()
			{
				Options options = new Options();
				// @formatter:off
				OptionHelper.addL(options, OPTION_DETAILS, false, false, "whether to print details");
				// @formatter:on
				return new CommonsCliExeOptions(options, "[options]");
			}

		};

		public static void main(String name, CommonsCliArguments arguments)
				throws IOException, ArgumentParseException
		{
			CommandLine line = arguments.getLine();

			boolean printDetails = line.hasOption(OPTION_DETAILS);

			String[] args = line.getArgs();
			logger.info("checking {} files", args.length);
			CheckOrientation check = new CheckOrientation();
			for (String arg : args) {
				check.execute(Paths.get(arg), printDetails);
			}
		}

	}

	public static class Fix
	{

		public static void main(String name, String[] args) throws IOException
		{
			logger.info("fixing {} files", args.length);
			FixOrientation fix = new FixOrientation();
			for (String arg : args) {
				fix.execute(Paths.get(arg));
			}
		}

	}

}
