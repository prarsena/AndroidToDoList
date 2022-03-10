package com.example.todolist;

public class ActivityData{
        private int position;
        private String act;
        //constructors
        public ActivityData(int num, String ac) {
            this.position = num;
            this.act = ac;
        }

        //accessors and mutators
        public int getPosition() {
            return position;
        }
        public void setPosition(int num) {
            this.position = num;
        }
        public String getActivity() {
            return act;
        }
        public void setActivity(String act) {
            this.act = act;
        }
}
