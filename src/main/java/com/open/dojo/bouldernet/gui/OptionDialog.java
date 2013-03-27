package com.open.dojo.bouldernet.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.open.dojo.bouldernet.gui.themes.Theme;
import com.open.dojo.bouldernet.gui.themes.ThemeManager;

public class OptionDialog extends JDialog {

	private static final long serialVersionUID = -6357060251598212477L;
	
	private class OkAction extends AbstractAction {
		
		public OkAction() {
			super("Ok");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			themeManager.setTheme((Theme)themesComboBox.getSelectedItem());
			close();
		}
	}
	
	private class CancelAction extends AbstractAction {
		
		public CancelAction() {
			super("Cancel");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
		
	}
	
	private static class ThemeCellRenderer extends JLabel implements ListCellRenderer {

		private static final long serialVersionUID = -1812385726600219687L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value != null) {
				Theme theme = (Theme) value;
				setText(theme.getName());
			}
			return this;
		}
		
	}

	private ThemeManager themeManager;
	private JComboBox themesComboBox;

	public OptionDialog(JFrame frame, ThemeManager themeManager) {
		super(frame, true);
		
		this.themeManager = themeManager;
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.PAGE_AXIS));
		List<Theme> themes = themeManager.getThemes();
		themesComboBox = new JComboBox(themes.toArray(new Theme[themes.size()]));
		themesComboBox.setSelectedItem(themeManager.getCurrentTheme());
		themesComboBox.setRenderer(new ThemeCellRenderer());
		formPanel.add(themesComboBox);
		
		getContentPane().add(formPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(new JButton(new OkAction()));
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(new JButton(new CancelAction()));
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
	}
	
	private void close() {
		dispose();
	}
}
