package cn.jpush.dubbo.thrift.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.thrift.TServiceClient;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
import org.springframework.remoting.rmi.RmiInvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

/**
 * todo 改改改
 * Created by rocyuan on 16/1/21.
 */
public class ThriftServiceClientInterceptor implements MethodInterceptor {
    private String serviceUrl;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        TServiceClient stub = this.getStub();

        try {
            return this.doInvoke(methodInvocation, stub);
        } catch (RemoteConnectFailureException var4) {
            return this.handleTServiceClientConnectFailure(methodInvocation, var4);
        } catch (RemoteException var5) {
            if(this.isConnectFailure(var5)) {
                return this.handleTServiceClientConnectFailure(methodInvocation, null);
            } else {
                throw var5;
            }
        }

    }

    private boolean isConnectFailure(RemoteException var5) {
        return true;
    }

    private Object handleTServiceClientConnectFailure(MethodInvocation methodInvocation, RemoteConnectFailureException var4) {
        return null;
    }


    protected Object doInvoke(MethodInvocation invocation, TServiceClient stub) throws Throwable {
        Throwable targetEx;
        if(stub instanceof RmiInvocationHandler) {

        } else {
            try {
                return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
            } catch (InvocationTargetException var9) {
                targetEx = var9.getTargetException();
                if(targetEx instanceof RemoteException) {
                    RemoteException rex = (RemoteException)targetEx;
                    throw RmiClientInterceptorUtils.convertRmiAccessException(invocation.getMethod(), rex, this.isConnectFailure(rex), this.getServiceUrl());
                } else {
                    throw targetEx;
                }
            }
        }
        return null;
    }
    public TServiceClient getStub() {
        return null;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }
}