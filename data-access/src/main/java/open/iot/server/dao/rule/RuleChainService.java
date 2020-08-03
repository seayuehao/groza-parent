package open.iot.server.dao.rule;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.id.RuleChainId;
import open.iot.server.common.data.id.RuleNodeId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageData;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.common.data.relation.EntityRelation;
import open.iot.server.common.data.rule.RuleChain;
import open.iot.server.common.data.rule.RuleChainMetaData;
import open.iot.server.common.data.rule.RuleNode;

import java.util.List;


public interface RuleChainService {

    RuleChain saveRuleChain(RuleChain ruleChain);

    boolean setRootRuleChain(RuleChainId ruleChainId);

    RuleChainMetaData saveRuleChainMetaData(RuleChainMetaData ruleChainMetaData);

    RuleChainMetaData loadRuleChainMetaData(RuleChainId ruleChainId);

    RuleChain findRuleChainById(RuleChainId ruleChainId);

    RuleNode findRuleNodeById(RuleNodeId ruleNodeId);

    ListenableFuture<RuleChain> findRuleChainByIdAsync(RuleChainId ruleChainId);

    ListenableFuture<RuleNode> findRuleNodeByIdAsync(RuleNodeId ruleNodeId);

    RuleChain getRootTenantRuleChain(TenantId tenantId);

    List<RuleNode> getRuleChainNodes(RuleChainId ruleChainId);

    List<EntityRelation> getRuleNodeRelations(RuleNodeId ruleNodeId);

    TextPageData<RuleChain> findTenantRuleChains(TenantId tenantId, TextPageLink pageLink);

    void deleteRuleChainById(RuleChainId ruleChainId);

    void deleteRuleChainsByTenantId(TenantId tenantId);
}
