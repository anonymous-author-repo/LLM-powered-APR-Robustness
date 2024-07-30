import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.comments.LineComment;

import java.util.Random;
import java.util.UUID;

public class AddLineCommentModifier extends VoidVisitorAdapter<Void> {
    private final Random random = new Random();

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);

        if (n.getBody().isPresent()) {
            BlockStmt body = n.getBody().get();

            // ����һ��Ψһ��ע������
            String commentContent = "This method was modified - " + UUID.randomUUID().toString();

            // ����һ����ע�ͽڵ�
            LineComment lineComment = new LineComment(commentContent);

            // ���ѡ��һ�������
            int insertIndex = random.nextInt(body.getStatements().size());

            // �����λ�õ���������ע��
            body.getStatements().get(insertIndex).setComment(lineComment);
        }
    }
}
