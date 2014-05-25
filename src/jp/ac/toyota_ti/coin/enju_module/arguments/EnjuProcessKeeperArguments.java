package jp.ac.toyota_ti.coin.enju_module.arguments;

import jp.ac.toyota_ti.coin.utils.Arguments;
import org.kohsuke.args4j.Option;

public class EnjuProcessKeeperArguments implements Arguments{
	@Option(name="--enju", metaVar="<Enju full path>")
	private static String EnjuFullPath;
	
	@Option(name="--port", metaVar="<Port>")
	private static Integer PortNumber;
	
	@Option(name="-h", aliases="--help", usage="Print usage message and exit.")
	private static boolean UsageFlag;

	@Option(name="-o", usage="Use Enju Options.")
	private static String Options;
	public String getEnjuPath(){
		return EnjuFullPath;
	}
	public Integer getPortNumber(){
		return PortNumber;
	}
	public String getOptions(){
		return Options;
	}
	
	@Override
	public boolean getUsageFlag() {
		return UsageFlag;
	}
}
