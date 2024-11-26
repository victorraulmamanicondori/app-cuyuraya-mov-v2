package com.eas.app.api.response;

import java.util.List;

public class LecturaPaginadoResponse {
    private int total;
    private int page;
    private int limit;
    private List<LecturaActualResponse> resultados;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<LecturaActualResponse> getResultados() {
        return resultados;
    }

    public void setResultados(List<LecturaActualResponse> resultados) {
        this.resultados = resultados;
    }
}
