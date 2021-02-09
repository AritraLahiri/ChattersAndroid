package aritra.code.chatters.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import aritra.code.chatters.Fragments.ChatFragment;
import aritra.code.chatters.Fragments.GroupsFragment;
import aritra.code.chatters.Fragments.NewsFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new ChatFragment();
            case 1:
                return new NewsFragment();
            case 2:
                return new GroupsFragment();
            default:
                return new ChatFragment();

        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;

        switch (position) {
            case 0:
                title = "";
                break;
            case 1:
                title = "";
                break;
            case 2:
                title = "";
                break;
            default:
                title = null;
                break;
        }
        return title;
    }
}
