//package shousi.cider.a;
//
//import java.util.*;
//
//public class Main {
//
//    private List<List<Integer>> res;
//    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
//        res=new ArrayList<>();
//        new ArrayList<>(res);
//        if(root==null){
//            return res;
//        }
//        findPath(root, new ArrayList<Integer>(), 0, targetSum);
//        return res;
//    }
//
//    private void findPath(TreeNode node, List<Integer> list, int sum, int targetSum){
//        if(sum>targetSum){
//            return;
//        }
//        list.add(node.val);
//        sum+=node.val;
//        if(node.left==null && node.right==null){
//            if(sum==targetSum){
//                res.add(list);
//            }
//        }else if(node.left==null){
//            findPath(node.right, list, sum, targetSum);
//        }else if(node.right==null){
//            findPath(node.left, list, sum, targetSum);
//        }else{
//            findPath(node.right, list, sum, targetSum);
//            findPath(node.left, list.clone(), sum, targetSum);
//        }
//    }
//    private static List<List<Integer>> list;
//    public static void main(String[] args) {
//        list=new ArrayList<>();
//    }
//    Thread a=new Thread(() -> {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    });
//    Thread b=new Thread(() -> {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    });
//
//}
