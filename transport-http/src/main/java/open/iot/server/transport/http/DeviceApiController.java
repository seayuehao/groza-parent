package open.iot.server.transport.http;

import com.datastax.driver.core.utils.UUIDs;
import com.google.gson.JsonParser;
import open.iot.server.common.data.DataConstants;
import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.kv.AttributeKvEntry;
import open.iot.server.common.data.kv.BaseAttributeKvEntry;
import open.iot.server.common.data.kv.KvEntry;
import open.iot.server.common.data.kv.StringDataEntry;
import open.iot.server.common.transport.SessionMsgProcessor;
import open.iot.server.common.transport.adaptor.JsonConverter;
import open.iot.server.common.transport.auth.DeviceAuthService;
import open.iot.server.common.transport.quota.host.HostRequestsQuotaService;
import open.iot.server.dao.attributes.AttributesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class DeviceApiController {

    @Value("${http.request_timeout}")
    private long defaultTimeout;

    @Autowired(required = false)
    private SessionMsgProcessor processor;

    @Autowired(required = false)
    private DeviceAuthService authService;

    @Autowired(required = false)
    private HostRequestsQuotaService quotaService;

    @Autowired
    private AttributesService attributesService;

    @RequestMapping(value = "/{deviceToken}/attributes", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity> postDeviceAttributes(
            @PathVariable("deviceToken") String deviceToken,
            @RequestBody String json) {
        DeferredResult<ResponseEntity> responseWriter = new DeferredResult<ResponseEntity>();
        responseWriter.setResult(new ResponseEntity<>(HttpStatus.ACCEPTED));
        Set<AttributeKvEntry> attributeKvEntrySet = JsonConverter.convertToAttributes(new JsonParser().parse(json)).getAttributes();
        for (AttributeKvEntry attributeKvEntry : attributeKvEntrySet) {
            System.out.println("属性名=" + attributeKvEntry.getKey() + " 属性值=" + attributeKvEntry.getValueAsString());
        }
        return responseWriter;
    }

    @RequestMapping(value = "/{deviceToken}/telemetry", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity> postTelemetry(@PathVariable("deviceToken") String deviceToken,
                                                        @RequestBody String json) {
        DeferredResult<ResponseEntity> responseWriter = new DeferredResult<ResponseEntity>();
        Map<Long, List<KvEntry>> data = JsonConverter.convertToTelemetry(new JsonParser().parse(json)).getData();
        for (Long key : data.keySet()) {
            System.out.println("上传数据时间 = " + key);
        }
        for (List<KvEntry> kvEntryList : data.values()) {
            for (int i = 0; i < kvEntryList.size(); i++) {
                System.out.println("上传数据属性=" + kvEntryList.get(i).getKey() + " 上传数据值=" + kvEntryList.get(i).getValueAsString());
            }
        }
        return responseWriter;
    }

    @RequestMapping(value = "/testAttribute", method = RequestMethod.POST)
    public void testAttributeKvEntity() throws ExecutionException, InterruptedException {
        DeviceId deviceId = new DeviceId(UUIDs.timeBased());
        KvEntry attrNewValue = new StringDataEntry("attribute5", "value5");
        AttributeKvEntry attrNew = new BaseAttributeKvEntry(attrNewValue, 73L);
        attributesService.save(deviceId, DataConstants.CLIENT_SCOPE, Collections.singletonList(attrNew)).get();
    }

}
