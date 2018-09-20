package shiyiliang.cn.basetool.util;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.List;

/**
 * @author WANG
 */
public class FragmentUtil {
    /**
     * 添加Fragment
     *
     * @param fm
     * @param container 容器的ID
     */
    public static void changeFragment(FragmentManager fm, int container, List<Fragment> fragments, int position) {

        FragmentTransaction transaction = fm.beginTransaction();

        for (int i = 0; i < fragments.size(); i++) {

            Fragment f = fragments.get(i);

            if (i == position) {

                if (f.isAdded()) {
                    transaction.show(f);
                } else {
                    transaction.add(container, fragments.get(i), fragments.get(i).getClass().getName());
                }

            } else {

                if (f.isAdded()) {
                    transaction.hide(f);
                }

            }
        }

        transaction.commitAllowingStateLoss();
    }

    /**
     * Fragment的切换
     *
     * @param fm
     * @param fragments 所有的Fragment
     * @param tagOfShow 需要即将显示的Fragment的标记
     * @deprecated
     */

    public static void changeFragment(FragmentManager fm,
                                      List<Fragment> fragments, String tagOfShow) {
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            if (tagOfShow.equals(fragment.getTag())) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }

        transaction.commitAllowingStateLoss();

    }

    /**
     * Fragment的替换
     *
     * @param fm
     * @param container
     * @param fragment  即将显示的Fragment
     * @param tagOfShow 即将显示的Fragment的TAG
     */
    public static void replaceFragment(FragmentManager fm, int container,
                                       Fragment fragment, String tagOfShow) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(container, fragment, tagOfShow);
        transaction.commitAllowingStateLoss();
    }

}
