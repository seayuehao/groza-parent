/**
 * Copyright © 2016-2018 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package open.iot.server.common.data.rule;

import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.id.RuleChainId;
import lombok.Data;

/**
 * Created by ashvayka on 21.03.18.
 * 规则链连接信息
 */
@Data
public class RuleChainConnectionInfo {
    private int fromIndex;
    private RuleChainId targetRuleChainId;
    private JsonNode additionalInfo;
    private String type;
}