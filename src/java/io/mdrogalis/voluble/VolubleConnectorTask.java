package io.mdrogalis.voluble;

import java.util.Map;
import java.util.List;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import org.apache.kafka.connect.source.SourceTask;
import org.apache.kafka.connect.source.SourceRecord;

public class VolubleConnectorTask extends SourceTask {

    private final static IFn require = Clojure.var("clojure.core", "require");
    private static String version;
    private IFn generateRecord;
    private Object context;

    static {
        require.invoke(Clojure.read("io.mdrogalis.voluble.interop"));
        version = (String) Clojure.var("io.mdrogalis.voluble.interop", "pom-version").invoke();
    }

    @Override
    public void start(Map<String, String> props) {
        IFn makeContext = Clojure.var("io.mdrogalis.voluble.interop", "make-context");
        context = makeContext.invoke(props);
        generateRecord = Clojure.var("io.mdrogalis.voluble.interop", "generate-source-record");
    }

    @Override
    public void stop() {
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        return (List) generateRecord.invoke(context);
    }

    @Override
    public String version() {
        return version;
    }
    
}
