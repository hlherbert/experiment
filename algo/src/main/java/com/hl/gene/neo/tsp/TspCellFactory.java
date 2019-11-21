package com.hl.gene.neo.tsp;

import com.hl.gene.neo.core.Cell;
import com.hl.gene.neo.core.CellFactory;

public class TspCellFactory implements CellFactory {

    private TspProblem problem;

    public void setProblem(TspProblem problem) {
        this.problem = problem;
    }

    @Override
    public Cell createCell() {
        return new TspCell(problem);
    }
}
