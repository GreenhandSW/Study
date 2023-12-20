package design;

public class Builder {
    /**
     * 产品类：面试
     */
    public static class Interview {
        private String interviewer; // 面试官
        private String interviewee; // 应聘人
        private String time; // 面试时间
        private String place; // 面试地点

        public void setInterviewer(String interviewer) {
            this.interviewer = interviewer;
        }

        public void setInterviewee(String interviewee) {
            this.interviewee = interviewee;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        @Override
        public String toString() {
            return String.format("time:%s\nplace:%s\ninterviewer:%s\ninterviewee:%s\n", time, place, interviewer, interviewee);
        }
    }

    /**
     * 抽象建造者类：面试构建者
     */
    public static abstract class InterviewBuilder {
        protected Interview interview = new Interview();

        public abstract void buildTime();

        public abstract void buildPlace();

        public abstract void buildInterviewer();

        public abstract void buildInterviewee();

        public Interview getInterview() {
            return interview;
        }
    }

    public static class OnlineInterviewBuilder extends InterviewBuilder {
        @Override
        public void buildTime() {
            interview.setTime("今天");
        }

        @Override
        public void buildPlace() {
            interview.setPlace("腾讯会议");
        }

        @Override
        public void buildInterviewer() {
            interview.setInterviewer("马化腾");
        }

        @Override
        public void buildInterviewee() {
            interview.setInterviewee("张三");
        }
    }

    public static class OnsiteInterviewBuilder extends InterviewBuilder {
        @Override
        public void buildTime() {
            interview.setTime("明天");
        }

        @Override
        public void buildPlace() {
            interview.setPlace("会议室");
        }

        @Override
        public void buildInterviewer() {
            interview.setInterviewer("马云");
        }

        @Override
        public void buildInterviewee() {
            interview.setInterviewee("李四");
        }
    }

    public static class Director {
        private final InterviewBuilder builder;

        public Director(InterviewBuilder builder) {
            this.builder = builder;
        }

        public Interview makeInterview() {
            builder.buildInterviewee();
            builder.buildInterviewer();
            builder.buildPlace();
            builder.buildTime();
            return builder.getInterview();
        }
    }

    public static void main(String[] args) {
        InterviewBuilder onlineInterviewBuilder = new OnlineInterviewBuilder();
        Director onlineDirector = new Director(onlineInterviewBuilder);
        Interview onlineInterview = onlineDirector.makeInterview();
        System.out.println(onlineInterview);

        InterviewBuilder onsiteInterviewBuilder = new OnsiteInterviewBuilder();
        Director onsiteDirector = new Director(onsiteInterviewBuilder);
        Interview onsiteInterview = onsiteDirector.makeInterview();
        System.out.println(onsiteInterview);
    }
}
