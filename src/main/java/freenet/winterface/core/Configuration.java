package freenet.winterface.core;

import freenet.config.ConfigCallback;
import freenet.config.InvalidConfigValueException;
import freenet.config.NodeNeedRestartException;
import freenet.config.SubConfig;
import freenet.support.api.BooleanCallback;
import freenet.support.api.IntCallback;
import freenet.support.api.LongCallback;
import freenet.support.api.StringCallback;

import java.util.concurrent.TimeUnit;

/**
 * Manages plugin configurations
 * 
 * @author pausb
 * 
 */
public class Configuration {
	
	/** Localization for plugin config page in fred */
	private final static I18n i18n = new I18n();

	/** Server port */
	private int port;
	/** Server idle timeout */
	private int idleTimeout;
	/** If it is public gateway */
	private boolean isPublicGateway;
	/** Allowed hosts */
	private String allowedHosts;
	/** Full access hosts */
	private String fullAccessHosts;
	/** Bind to addresses */
	private String bindTo;
	/** Maximum size for transparent pass-through */
	private long maxLength;

	/** Default server port value */
	private final static int PORT_DEFAULT = 8088;
	/** Port entry name in config file */
	private final static String PORT_OPTION = "port";

	/** Default server idle timeout */
	private final static int IDLE_TIEMOUT_DEFAULT = (int)TimeUnit.HOURS.toMillis(1);
	/** Server idle timeout entry name in config file */
	private final static String IDLE_TIMEOUT_OPTION = "idleTimeout";

	/** Default public gateway mode */
	private final static boolean GATEWAYMODE_DEFAULT = false;
	/** Public gateway mode entry name in config file */
	private final static String GATEWAYMODE_OPTION = "isPublicGateWay";

	/** Default allowed hosts */
	private final static String ALLOWED_HOSTS_DEFAULT = "127.0.0.1,0:0:0:0:0:0:0:1";
	/** Allowed hosts entry name in config file */
	private final static String ALLOWED_HOSTS_OPTION = "allowedHosts";

	/** Default full access hosts */
	private final static String FULLACCESS_HOSTS_DEFAULT = "127.0.0.1,0:0:0:0:0:0:0:1";
	/** Full access hosts entry name in config file */
	private final static String FULLACCESS_HOSTS_OPTION = "allowedHostsFullAccess";

	/** Default bindto hosts */
	private final static String BINDTO_DEFAULT = "127.0.0.1";
	/** Bindto hosts entry name in config file */
	private final static String BINDTO_OPTION = "bindTo";

	/** Defualt MaxLength value (2MB plus a bit due to buggy inserts) */
	private final static long MAXLENGTH_DEFAULT = (2 * 1024 * 1024 * 11) / 10;
	/** MaxLength entry name in config file */
	private final static String MAXLENGTH_OPTION = "maxLength";

	/**
	 * {@link ConfigCallback} for gate public way mode
	 * 
	 * @author pausb
	 */
	class PublicGatewayOption extends BooleanCallback {

		@Override
		public Boolean get() {
			return isPublicGateway;
		}

		@Override
		public void set(Boolean val) throws InvalidConfigValueException, NodeNeedRestartException {
			isPublicGateway = val;
		}
	}

	/**
	 * {@link ConfigCallback} for server port
	 * 
	 * @author pausb
	 * 
	 */
	class PortOption extends IntCallback {

		@Override
		public Integer get() {
			return port;
		}

		@Override
		public void set(Integer val) throws InvalidConfigValueException, NodeNeedRestartException {
			port = val;
		}
	}

	/**
	 * {@link ConfigCallback} for server idle timeout
	 * 
	 * @author pausb
	 * 
	 */
	class TimeoutOption extends IntCallback {

		@Override
		public Integer get() {
			return idleTimeout;
		}

		@Override
		public void set(Integer val) throws InvalidConfigValueException, NodeNeedRestartException {
			idleTimeout = val;
		}

	}

	/**
	 * {@link ConfigCallback} for allowed hosts
	 * 
	 * @author pausb
	 * 
	 */
	class AllowedHosts extends StringCallback {

		@Override
		public String get() {
			return allowedHosts;
		}

		@Override
		public void set(String val) throws InvalidConfigValueException, NodeNeedRestartException {
			if (!isHostListValid(val)) {
				throw new InvalidConfigValueException("Host list contains illegal characters.");
			}
			allowedHosts = val;
			throw new NodeNeedRestartException("Winterface server needs to be restarted.");
		}

	}

	/**
	 * {@link ConfigCallback} for full access hosts
	 * 
	 * @author pausb
	 * 
	 */
	class FullAccessHosts extends StringCallback {

		@Override
		public String get() {
			return fullAccessHosts;
		}

		@Override
		public void set(String val) throws InvalidConfigValueException, NodeNeedRestartException {
			if (!isHostListValid(val)) {
				throw new InvalidConfigValueException("Host list contains illegal characters.");
			}
			fullAccessHosts = val;
		}

	}

	/**
	 * {@link ConfigCallback} for bind to hosts
	 * 
	 * @author pausb
	 * 
	 */
	class BindToHosts extends StringCallback {

		@Override
		public String get() {
			return bindTo;
		}

		@Override
		public void set(String val) throws InvalidConfigValueException, NodeNeedRestartException {
			if (!isHostListValid(val)) {
				throw new InvalidConfigValueException("Host list contains illegal characters.");
			}
			bindTo = val;
		}

	}

	/**
	 * {@link ConfigCallback} for max length
	 * 
	 * @author pausb
	 * 
	 */

	class MaxLength extends LongCallback {

		@Override
		public Long get() {
			return maxLength;
		}

		@Override
		public void set(Long val) throws InvalidConfigValueException, NodeNeedRestartException {
			maxLength = val;
		}

	}

	/**
	 * Initializes {@link SubConfig} passed by Freenet before
	 * {@link WinterfacePlugin} starts
	 * 
	 * @param subConfig
	 *            {@link SubConfig} to be initialized
	 */
	void initialize(SubConfig subConfig) {
		short sortOrder = 0;
		// FIXME what is the last parameter? (isSize)
		subConfig.register(PORT_OPTION, PORT_DEFAULT, sortOrder, true, false, shortDesc(PORT_OPTION), longDesc(PORT_OPTION), new PortOption(), false);
		port = subConfig.getInt(PORT_OPTION);
		subConfig.register(GATEWAYMODE_OPTION, GATEWAYMODE_DEFAULT, ++sortOrder, true, false, shortDesc(GATEWAYMODE_OPTION), longDesc(GATEWAYMODE_OPTION),
				new PublicGatewayOption());
		isPublicGateway = subConfig.getBoolean(GATEWAYMODE_OPTION);
		subConfig.register(IDLE_TIMEOUT_OPTION, IDLE_TIEMOUT_DEFAULT, ++sortOrder, true, false, shortDesc(IDLE_TIMEOUT_OPTION), longDesc(IDLE_TIMEOUT_OPTION),
				new TimeoutOption(), false);
		idleTimeout = subConfig.getInt(IDLE_TIMEOUT_OPTION);
		subConfig.register(ALLOWED_HOSTS_OPTION, ALLOWED_HOSTS_DEFAULT, ++sortOrder, true, false, shortDesc(ALLOWED_HOSTS_OPTION),
				longDesc(ALLOWED_HOSTS_OPTION), new AllowedHosts());
		allowedHosts = subConfig.getString(ALLOWED_HOSTS_OPTION);
		subConfig.register(FULLACCESS_HOSTS_OPTION, FULLACCESS_HOSTS_DEFAULT, ++sortOrder, true, false, shortDesc(FULLACCESS_HOSTS_OPTION),
				longDesc(FULLACCESS_HOSTS_OPTION), new FullAccessHosts());
		fullAccessHosts = subConfig.getString(FULLACCESS_HOSTS_OPTION);
		subConfig.register(BINDTO_OPTION, BINDTO_DEFAULT, ++sortOrder, true, false, shortDesc(BINDTO_OPTION), longDesc(BINDTO_OPTION), new BindToHosts());
		bindTo = subConfig.getString(BINDTO_OPTION);
		subConfig.register(MAXLENGTH_OPTION, MAXLENGTH_DEFAULT, ++sortOrder, true, false, shortDesc(MAXLENGTH_OPTION), longDesc(MAXLENGTH_OPTION),
				new MaxLength(), false);
		maxLength = subConfig.getLong(MAXLENGTH_OPTION);
	}

	/**
	 * Return short description key for localization
	 * 
	 * @param optionName
	 *            option name
	 * @return key for localization
	 */
	private String shortDesc(String optionName) {
		return "Config." + optionName;
	}

	/**
	 * Return long description key for localization
	 * 
	 * @param optionName
	 *            option name
	 * @return key for localization
	 */
	private String longDesc(String optionName) {
		return i18n.get("Config." + optionName + "Long");
	}

	/**
	 * Returns server port
	 * 
	 * @return server port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns server idle timeout
	 * 
	 * @return server idle timeout
	 */
	public int getIdleTimeout() {
		return idleTimeout;
	}

	/**
	 * {@code true} if Winterface is in public gateway mode
	 * 
	 * @return {@code false} if not in public gateway mode
	 */
	public boolean isPublicGateway() {
		return isPublicGateway;
	}

	/**
	 * Returns a list of allowed hosts to access Winterface
	 * 
	 * @return comma separated list of allowed hosts
	 */
	public String getAllowedHosts() {
		return allowedHosts;
	}

	/**
	 * Returns a list of allowed hosts which have full access to Winterface
	 * 
	 * @return comma separated list of hosts with full access hosts
	 */
	public String getFullAccessHosts() {
		return fullAccessHosts;
	}

	/**
	 * Returns a list of hosts to bind server to
	 * 
	 * @return comma separated list of hosts to bind to
	 */
	public String getBindToHosts() {
		return bindTo;
	}

	/**
	 * Returns Maximum size for transparent pass-through
	 * 
	 * @return max length
	 */
	public long getMaxLength() {
		return maxLength;
	}
	
	/**
	 * Checks a comma separated list of hosts (IPs) for validity.
	 * 
	 * @param hostList
	 *            comma separated list of IPs to check
	 * @return {@code true} if all IPs are valid
	 * @see IPUtils#isValid(String)
	 */
	private boolean isHostListValid(String hostList) {
		String[] hosts = hostList.split("\\,");
		for (String host : hosts) {
			if (!IPUtils.isValid(host)) {
				return false;
			}
		}
		return true;
	}

}
