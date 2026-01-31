package com.connect.transitconnect.service;

import com.connect.transitconnect.dto.*;
import com.connect.transitconnect.entity.*;
import com.connect.transitconnect.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // ============================
    // DTO → Entity Converters
    // ============================
    private StopEntity toStopEntity(StopDTO dto) {
        StopEntity e = new StopEntity();
        e.setLocation(dto.getLocation());
        e.setLatitude(dto.getLatitude());
        e.setLongitude(dto.getLongitude());
        return e;
    }

    private HopEntity toHopEntity(HopDTO dto) {
        HopEntity h = new HopEntity();
        h.setCost(dto.getCost());
        h.setDuration(dto.getDuration());
        h.setMode(dto.getMode());
        return h;
    }

    // ============================
    // SAVE ROUTE
    // ============================
    public RouteEntity saveRoute(RouteInputDTO dto) {
        List<StopDTO> stopDTOs = dto.getStops();
        List<HopDTO> hopDTOs = dto.getHops();

        if (stopDTOs == null || stopDTOs.size() < 2)
            throw new IllegalArgumentException("Route must have at least 2 stops");

        if (hopDTOs == null || hopDTOs.size() != stopDTOs.size() - 1)
            throw new IllegalArgumentException("Hops must be exactly stops.size() - 1");

        List<StopEntity> stopEntities = stopDTOs.stream()
                .map(this::toStopEntity)
                .collect(Collectors.toList());

        List<HopEntity> hopEntities = IntStream.range(0, hopDTOs.size())
                .mapToObj(i -> {
                    HopEntity h = toHopEntity(hopDTOs.get(i));
                    h.setFromStop(stopEntities.get(i));
                    h.setToStop(stopEntities.get(i + 1));
                    return h;
                }).collect(Collectors.toList());

        RouteEntity route = new RouteEntity();
        route.setStops(stopEntities);
        route.setHops(hopEntities);

        return routeRepository.save(route);
    }

    // ============================
    // BASIC OPERATIONS
    // ============================
    public List<RouteEntity> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<RouteEntity> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    // ============================
    // OLD smartSearch (unchanged)
    // ============================
    // public Optional<Object> smartSearch(String qStop1, String qStop2) {
    //
    // if (qStop1 == null || qStop2 == null) return Optional.empty();
    //
    // String stop1 = qStop1.toLowerCase().trim();
    // String stop2 = qStop2.toLowerCase().trim();
    //
    // List<RouteEntity> allRoutes = routeRepository.findAll();
    //
    // for (RouteEntity route : allRoutes) {
    // List<StopEntity> stops = route.getStops();
    //
    // List<String> locs = stops.stream()
    // .map(s -> s.getLocation().toLowerCase())
    // .collect(Collectors.toList());
    //
    // int idx1 = -1, idx2 = -1;
    // for (int i = 0; i < locs.size(); i++) {
    // if (locs.get(i).contains(stop1)) idx1 = i;
    // if (locs.get(i).contains(stop2)) idx2 = i;
    // }
    //
    // if (idx1 == -1 || idx2 == -1) continue;
    //
    // int startIndex = Math.min(idx1, idx2);
    // int endIndex = Math.max(idx1, idx2);
    //
    // if (startIndex == 0 && endIndex == stops.size() - 1)
    // return Optional.of(route);
    //
    // List<StopEntity> segStops = stops.subList(startIndex, endIndex + 1);
    // List<HopEntity> routeHops = route.getHops();
    // List<HopEntity> segHops = routeHops.subList(startIndex, endIndex);
    //
    // List<StopDTO> segStopDTOs = segStops.stream().map(s -> {
    // StopDTO sd = new StopDTO();
    // sd.setLocation(s.getLocation());
    // sd.setLatitude(s.getLatitude());
    // sd.setLongitude(s.getLongitude());
    // return sd;
    // }).collect(Collectors.toList());
    //
    // List<HopDTO> segHopDTOs = segHops.stream().map(h -> {
    // HopDTO hd = new HopDTO();
    // hd.setCost(h.getCost());
    // hd.setMode(h.getMode());
    // return hd;
    // }).collect(Collectors.toList());
    //
    // int totalCost = segHopDTOs.stream()
    // .mapToInt(hd -> hd.getCost() == null ? 0 : hd.getCost())
    // .sum();
    //
    // RouteSegmentDTO segDTO = new RouteSegmentDTO();
    // segDTO.setSegmentStops(segStopDTOs);
    // segDTO.setSegmentHops(segHopDTOs);
    // segDTO.setTotalCost(totalCost);
    // segDTO.setStopsCount(segStopDTOs.size());
    //
    // return Optional.of(segDTO);
    // }
    //
    // return Optional.empty();
    // }

    // ========================================================================
    // MULTI-ROUTE GRAPH BASED SEARCH ENGINE
    // - findShortestPath() → minimum hops
    // - findMinCostPath() → minimum cost
    // ========================================================================

    private static class Edge {
        final String to;
        final Integer cost;
        final Integer duration;
        final String mode;

        Edge(String to, Integer cost, Integer duration, String mode) {
            this.to = to;
            this.cost = cost;
            this.duration = duration;
            this.mode = mode;
        }
    }

    private static class Node {
        String loc;
        int weight; // can be cost or duration

        Node(String l, int w) {
            loc = l;
            weight = w;
        }
    }

    private Map<String, List<Edge>> buildGraph(Map<String, StopEntity> locToEntity) {

        Map<String, List<Edge>> graph = new HashMap<>();
        List<RouteEntity> allRoutes = routeRepository.findAll();

        for (RouteEntity route : allRoutes) {
            List<StopEntity> stops = route.getStops();
            List<HopEntity> hops = route.getHops();

            if (stops == null || stops.size() < 2)
                continue;

            List<String> locs = stops.stream()
                    .map(s -> s.getLocation().toLowerCase().trim())
                    .collect(Collectors.toList());

            for (int i = 0; i < stops.size(); i++) {
                String loc = locs.get(i);
                locToEntity.putIfAbsent(loc, stops.get(i));
                graph.putIfAbsent(loc, new ArrayList<>());
            }

            for (int i = 0; i < locs.size() - 1; i++) {
                String u = locs.get(i);
                String v = locs.get(i + 1);

                Integer cost = hops.get(i).getCost();
                Integer duration = hops.get(i).getDuration();
                String mode = hops.get(i).getMode();

                graph.get(u).add(new Edge(v, cost, duration, mode));
                graph.get(v).add(new Edge(u, cost, duration, mode)); // reverse travel enabled
            }
        }
        return graph;
    }

    // ==========================================================
    // SHORTEST PATH (MINIMUM HOPS)
    // ==========================================================
    public Optional<Object> findShortestPath(String qStop1, String qStop2) {

        if (qStop1 == null || qStop2 == null)
            return Optional.empty();
        String stop1 = qStop1.toLowerCase().trim();
        String stop2 = qStop2.toLowerCase().trim();

        Map<String, StopEntity> locToEntity = new HashMap<>();
        Map<String, List<Edge>> graph = buildGraph(locToEntity);

        Set<String> starts = graph.keySet().stream()
                .filter(s -> s.contains(stop1))
                .collect(Collectors.toSet());

        Set<String> ends = graph.keySet().stream()
                .filter(s -> s.contains(stop2))
                .collect(Collectors.toSet());

        if (starts.isEmpty() || ends.isEmpty())
            return Optional.empty();

        Queue<String> q = new ArrayDeque<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String s : starts) {
            q.add(s);
            visited.add(s);
            parent.put(s, null);
        }

        String found = null;

        while (!q.isEmpty()) {
            String curr = q.poll();
            if (ends.contains(curr)) {
                found = curr;
                break;
            }

            for (Edge e : graph.get(curr)) {
                if (!visited.contains(e.to)) {
                    visited.add(e.to);
                    parent.put(e.to, curr);
                    q.add(e.to);
                }
            }
        }

        if (found == null)
            return Optional.empty();

        List<String> path = new ArrayList<>();
        String cur = found;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);

        return Optional.of(buildSegmentDTOFromPath(path, graph, locToEntity));
    }

    // ==========================================================
    // FASTEST PATH (DIJKSTRA on DURATION)
    // ==========================================================
    public Optional<Object> findFastestPath(String qStop1, String qStop2) {

        if (qStop1 == null || qStop2 == null)
            return Optional.empty();
        String stop1 = qStop1.toLowerCase().trim();
        String stop2 = qStop2.toLowerCase().trim();

        Map<String, StopEntity> locToEntity = new HashMap<>();
        Map<String, List<Edge>> graph = buildGraph(locToEntity);

        Set<String> starts = graph.keySet().stream()
                .filter(s -> s.contains(stop1))
                .collect(Collectors.toSet());

        Set<String> ends = graph.keySet().stream()
                .filter(s -> s.contains(stop2))
                .collect(Collectors.toSet());

        if (starts.isEmpty() || ends.isEmpty())
            return Optional.empty();

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.weight));
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String s : starts) {
            dist.put(s, 0);
            parent.put(s, null);
            pq.add(new Node(s, 0));
        }

        String found = null;

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (visited.contains(node.loc))
                continue;
            visited.add(node.loc);

            if (ends.contains(node.loc)) {
                found = node.loc;
                break;
            }

            for (Edge e : graph.get(node.loc)) {
                int d = (e.duration == null ? 0 : e.duration);
                int newDist = node.weight + d;

                if (newDist < dist.getOrDefault(e.to, Integer.MAX_VALUE)) {
                    dist.put(e.to, newDist);
                    parent.put(e.to, node.loc);
                    pq.add(new Node(e.to, newDist));
                }
            }
        }

        if (found == null)
            return Optional.empty();

        List<String> path = new ArrayList<>();
        String cur = found;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);

        return Optional.of(buildSegmentDTOFromPath(path, graph, locToEntity));
    }

    // ==========================================================
    // MINIMUM COST PATH (DIJKSTRA)
    // ==========================================================
    public Optional<Object> findMinCostPath(String qStop1, String qStop2) {

        if (qStop1 == null || qStop2 == null)
            return Optional.empty();
        String stop1 = qStop1.toLowerCase().trim();
        String stop2 = qStop2.toLowerCase().trim();

        Map<String, StopEntity> locToEntity = new HashMap<>();
        Map<String, List<Edge>> graph = buildGraph(locToEntity);

        Set<String> starts = graph.keySet().stream()
                .filter(s -> s.contains(stop1))
                .collect(Collectors.toSet());

        Set<String> ends = graph.keySet().stream()
                .filter(s -> s.contains(stop2))
                .collect(Collectors.toSet());

        if (starts.isEmpty() || ends.isEmpty())
            return Optional.empty();

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.weight));
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String s : starts) {
            dist.put(s, 0);
            parent.put(s, null);
            pq.add(new Node(s, 0));
        }

        String found = null;

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (visited.contains(node.loc))
                continue;
            visited.add(node.loc);

            if (ends.contains(node.loc)) {
                found = node.loc;
                break;
            }

            for (Edge e : graph.get(node.loc)) {
                int c = (e.cost == null ? 0 : e.cost);
                int newCost = node.weight + c;

                if (newCost < dist.getOrDefault(e.to, Integer.MAX_VALUE)) {
                    dist.put(e.to, newCost);
                    parent.put(e.to, node.loc);
                    pq.add(new Node(e.to, newCost));
                }
            }
        }

        if (found == null)
            return Optional.empty();

        List<String> path = new ArrayList<>();
        String cur = found;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);

        return Optional.of(buildSegmentDTOFromPath(path, graph, locToEntity));
    }

    // ==========================================================
    // CONVERT PATH → RouteSegmentDTO
    // ==========================================================
    private RouteSegmentDTO buildSegmentDTOFromPath(
            List<String> path,
            Map<String, List<Edge>> graph,
            Map<String, StopEntity> locToEntity) {

        List<StopDTO> stopDTOs = new ArrayList<>();

        for (String loc : path) {
            StopEntity ent = locToEntity.get(loc);

            StopDTO sd = new StopDTO();
            if (ent != null) {
                sd.setLocation(ent.getLocation());
                sd.setLatitude(ent.getLatitude());
                sd.setLongitude(ent.getLongitude());
            } else {
                sd.setLocation(loc);
            }
            stopDTOs.add(sd);
        }

        List<HopDTO> hopDTOs = new ArrayList<>();
        int totalCost = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            String u = path.get(i);
            String v = path.get(i + 1);

            List<Edge> edges = graph.get(u).stream()
                    .filter(e -> e.to.equals(v))
                    .collect(Collectors.toList());

            Edge chosen = edges.stream()
                    .min(Comparator.comparingInt(e -> (e.cost == null ? 0 : e.cost)))
                    .orElse(null);

            int c = (chosen == null ? 0 : (chosen.cost == null ? 0 : chosen.cost));
            int d = (chosen == null ? 0 : (chosen.duration == null ? 0 : chosen.duration));

            HopDTO hd = new HopDTO();
            hd.setCost(c);
            hd.setDuration(d);
            hd.setMode(chosen == null ? null : chosen.mode);

            hopDTOs.add(hd);
            totalCost += c;
        }

        int totalDuration = hopDTOs.stream().mapToInt(h -> h.getDuration() == null ? 0 : h.getDuration()).sum();

        RouteSegmentDTO seg = new RouteSegmentDTO();
        seg.setSegmentStops(stopDTOs);
        seg.setSegmentHops(hopDTOs);
        seg.setTotalCost(totalCost);
        seg.setTotalDuration(totalDuration); // set total duration
        seg.setStopsCount(stopDTOs.size());

        return seg;
    }

    public List<String> getAllStopNames() {
        List<RouteEntity> routes = routeRepository.findAll();

        Set<String> names = new HashSet<>();

        for (RouteEntity r : routes) {
            for (StopEntity s : r.getStops()) {
                names.add(s.getLocation());
            }
        }

        return new ArrayList<>(names);
    }

}
