package lilja.kiiski.doitlist;

import android.os.Parcel;
import android.os.Parcelable;

/* TASK CLASS
- Basic task class
- Each task is one Task object
 */

public class Task implements Parcelable {
    String name;
    String details;
    String done = "false";
    int num; //ID num
    boolean expanded = false;

    public Task() {
    }

    protected Task(Parcel in) {
        name = in.readString();
        details = in.readString();
        done = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(details);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}