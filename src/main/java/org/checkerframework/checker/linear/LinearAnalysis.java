package org.checkerframework.checker.linear;

import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.dataflow.analysis.TransferResult;
import org.checkerframework.dataflow.cfg.node.AssignmentNode;
import org.checkerframework.dataflow.cfg.node.LocalVariableNode;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.framework.flow.CFAbstractAnalysis;
import org.checkerframework.framework.flow.CFStore;
import org.checkerframework.framework.flow.CFValue;

public class LinearAnalysis extends CFAbstractAnalysis<CFValue, CFStore, LinearTransfer> {

    public LinearAnalysis(BaseTypeChecker checker, LinearAnnotatedTypeFactory factory) {
        super(checker, factory);
    }

    @Override
    protected boolean updateNodeValues(Node node, TransferResult<CFValue, CFStore> transferResult) {
        CFValue newVal = transferResult.getResultValue();
        boolean nodeValueChanged = false;
        if (newVal != null) {
            CFValue oldVal = nodeValues.get(node);
            if (node instanceof AssignmentNode) {
                Node rhsNode = ((AssignmentNode) node).getExpression();
                if (rhsNode instanceof LocalVariableNode) {
                    oldVal = nodeValues.get(rhsNode);
                    nodeValues.put(rhsNode, newVal);
                }
            } else {
                nodeValues.put(node, newVal);
            }
            nodeValueChanged = !Objects.equals(oldVal, newVal);
        }
        return nodeValueChanged || transferResult.storeChanged();
    }

    @Override
    public CFStore createEmptyStore(boolean sequentialSemantics) {
        return new CFStore(this, sequentialSemantics);
    }

    @Override
    public CFStore createCopiedStore(CFStore s) {
        return new CFStore(s);
    }

    @Override
    public CFValue createAbstractValue(
            Set<AnnotationMirror> annotations, TypeMirror underlyingType) {
        return defaultCreateAbstractValue(this, annotations, underlyingType);
    }
}