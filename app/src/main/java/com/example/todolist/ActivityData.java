package com.example.todolist;

/* It wasn't strictly necessary to define a custom data type.
*  Originally, I defined two fields (position and activity) but then
*  realized the indexOf method for ArrayLists works better than manually
*  storing a position.
*
*  But, since it's here, it's useful to keep because it could always
*  be extended with other attributes. */
public class ActivityData{

        private String act;

        public ActivityData(String ac) {
            this.act = ac;
        }

        public String getActivity() {
            return act;
        }
        public void setActivity(String act) {
            this.act = act;
        }
}
