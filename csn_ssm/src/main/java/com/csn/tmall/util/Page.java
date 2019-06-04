package com.csn.tmall.util;



public class Page {

    private int total;
    private int count;
    private String param;
    private int start;

    private static final int defaultCount = 5; //默认每页显示5条

    public Page (){
        count = defaultCount;
    }
    public Page(int start, int count) {
        this();
        this.start = start;
        this.count = count;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public int gettotalPage(){

        //一共有多少页
        int totalPage;

        //如果总数被5整除
        if(total % count == 0)
            totalPage = total / count;

        else
            totalPage = total / count + 1;
        if(total == 0)
            totalPage = 1;

        return totalPage;
    }

    public int getLast(){

        //最后一页的第一个id号。
        int last;

        //假设总数是45，能被5整除，最后一页开始的id就是40。
        if(0 == total % count)
            last = total - count;
        //假设总数是46，不能被5整除，最后一页开始的应该是45.
        else
            last = total - total % count;

        last = last<0?0:last;
        return last;

    }

    public boolean gethasPreviouse(){
        if(start == 0)
            return false;
        else
            return true;
    }

    public boolean getHasNext(){

        if(start == getLast())
            return false;
        else
            return true;

    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", count=" + count +
                ", param=" + param +
                ", start=" + start +
                '}';
    }
}