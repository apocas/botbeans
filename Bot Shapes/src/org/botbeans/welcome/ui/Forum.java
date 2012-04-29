package org.botbeans.welcome.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.botbeans.welcome.Feeds;
import org.botbeans.welcome.content.Constants;
import org.botbeans.welcome.content.Feed;
import org.botbeans.welcome.content.FeedEvent;
import org.botbeans.welcome.content.FeedListener;
import org.botbeans.welcome.content.FeedMessage;
import org.botbeans.welcome.content.SpringUtilities;
import org.botbeans.welcome.content.WebLink;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Viorel
 */
public class Forum extends JPanel implements Constants, FeedListener {

    private JLabel loading;

    public Forum() {
        super(new SpringLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        Feeds.getDefault().addFeedListener(Feeds.forumFeed, (FeedListener) this);
    }

    private void initComponents() {
        JLabel label;

        label = new JLabel(ImageUtilities.loadImageIcon(FORUM_ICON, true));
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder());
        add(label);

        loading = new JLabel("Projects...");
        loading.setHorizontalAlignment(JLabel.LEFT);
        loading.setOpaque(false);
        loading.setBorder(BorderFactory.createEmptyBorder());
        loading.setFont(LINK_FONT);
        loading.setForeground(LINK_COLOR);
        add(loading);

        SpringUtilities.makeCompactGrid(this,
                getComponentCount(), 1,
                5, 10,
                5, 5);
    }

    @Override
    public void fireFeedParsed(FeedEvent event) {
        Feed feed = (Feed) event.getSource();
        if (feed.getFeedName().equals(Feeds.forumFeed)) {
            remove(loading);

            for (int i = 0; i < feed.getMessages().size(); i++) {
                FeedMessage message = feed.getMessages().get(i);
                add(WebLink.createWebLink(message.getTitle(), message.getLink(), true));
            }

            SpringUtilities.makeCompactGrid(this,
                    getComponentCount(), 1,
                    5, 5,
                    5, 5);

            revalidate();
            repaint();
        }
    }
}
