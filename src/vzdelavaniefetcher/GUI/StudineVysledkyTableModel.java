/*
 *  Copyright (c) 2015 Michal Ďuračík
 */
package vzdelavaniefetcher.GUI;

import java.awt.datatransfer.StringSelection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import vzdelavaniefetcher.StudijnyVysledok;

/**
 *
 * @author Unlink
 */
public class StudineVysledkyTableModel extends AbstractTableModel {

	private List<StudijnyVysledok> aData;

	private static DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");

	public StudineVysledkyTableModel(List<StudijnyVysledok> paData) {
		this.aData = paData;
		Collections.sort(aData, new Comparator<StudijnyVysledok>() {
			@Override
			public int compare(StudijnyVysledok paO1, StudijnyVysledok paO2) {
				return (-1) * paO1.getSemester().compareTo(paO2.getSemester());
			}
		});
	}

	@Override
	public int getRowCount() {
		return aData.size();
	}

	@Override
	public int getColumnCount() {
		return Columns.values().length;
	}

	@Override
	public Object getValueAt(int paRowIndex, int paColumnIndex) {
		return Columns.values()[paColumnIndex].getAt(aData.get(paRowIndex));
	}

	@Override
	public String getColumnName(int paColumn) {
		return Columns.values()[paColumn].getName();
	}

	private enum Columns {

		PREDMET("Predmet") {
				@Override
				public Object getAt(StudijnyVysledok paRow) {
					return paRow.getPredmet();
				}
			},
		SEMESTER("Semester") {
				@Override
				public Object getAt(StudijnyVysledok paRow) {
					return paRow.getSemester();
				}
			},
		POVINNOST("Pov.") {
				@Override
				public Object getAt(StudijnyVysledok paRow) {
					return paRow.getPov();
				}
			},
		KREDITOV("Kred.") {
				@Override
				public Object getAt(StudijnyVysledok paRow) {
					return paRow.getKreditov();
				}
			},
		SKUSKA("Skúška") {
				@Override
				public Object getAt(StudijnyVysledok paRow) {
					return (paRow.getSkuska() != null) ? dateFormat.format(paRow.getSkuska()) : "";
				}
			},
		ZNAMKA("Znamka") {
				@Override
				public Object getAt(StudijnyVysledok paRow) {
					return paRow.getSkuskaZn();
				}
			};

		private String aName;

		private Columns(String paName) {
			this.aName = paName;
		}

		public String getName() {
			return aName;
		}

		public abstract Object getAt(StudijnyVysledok row);

	}

}
