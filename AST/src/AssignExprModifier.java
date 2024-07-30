import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class AssignExprModifier extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);

        n.getBody().ifPresent(body -> {
            for (int i = 0; i < body.getStatements().size(); i++) {
                if (body.getStatement(i) instanceof ExpressionStmt) {
                    ExpressionStmt stmt = (ExpressionStmt) body.getStatement(i);
                    if (stmt.getExpression() instanceof AssignExpr) {
                        AssignExpr assignExpr = (AssignExpr) stmt.getExpression();
                        if (assignExpr.getValue() instanceof BinaryExpr) {
                            BinaryExpr binaryExpr = (BinaryExpr) assignExpr.getValue();
                            Expression left = binaryExpr.getLeft();
                            Expression right = binaryExpr.getRight();

                            // ��鸳ֵ���ʽ��Ŀ���Ƿ����Ԫ���ʽ��һ����ͬ
                            if (assignExpr.getTarget().equals(right) || assignExpr.getTarget().equals(left)) {
                                // ȷ����Ŀ����ʽ
                                Expression nonTargetExpr = assignExpr.getTarget().equals(left) ? right : left;

                                // �����µĸ��ϸ�ֵ���ʽ
                                AssignExpr newAssign = new AssignExpr(
                                        assignExpr.getTarget().clone(), nonTargetExpr.clone(), getCorrespondingAssignOperator(binaryExpr.getOperator()));

                                if (newAssign.getOperator() != null) { // ȷ���ж�Ӧ�Ĳ�����
                                    // �滻ԭʼ��ֵ���ʽ
                                    stmt.setExpression(newAssign);
                                    System.out.println("Replaced assign expression: " + assignExpr + " with " + newAssign);
                                } else {
                                    System.out.println("No corresponding operator found for: " + binaryExpr.getOperator());
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private AssignExpr.Operator getCorrespondingAssignOperator(BinaryExpr.Operator binaryOperator) {
        switch (binaryOperator) {
            case PLUS:
                return AssignExpr.Operator.PLUS;
            case MINUS:
                return AssignExpr.Operator.MINUS;
            case MULTIPLY:
                return AssignExpr.Operator.MULTIPLY;
            case DIVIDE:
                return AssignExpr.Operator.DIVIDE;
            // �������������Ը�����Ҫ���
            default:
                return null; // ����δ����Ĳ�����������null
        }
    }
}
