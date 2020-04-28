package open.iot.server.common.data.security;

public interface DeviceCredentialsFilter {

    String getCredentialsId();

    DeviceCredentialsType getCredentialsType();
}
