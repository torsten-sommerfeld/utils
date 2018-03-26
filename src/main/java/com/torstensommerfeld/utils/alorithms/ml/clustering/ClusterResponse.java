package com.torstensommerfeld.utils.alorithms.ml.clustering;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClusterResponse<T> {
    private List<T> notClustered;
    private List<List<T>> clusters;
}
