# Novelty and submission notes

## Current assessment

The package remains plausibly novel after a documented web and metadata search,
but the residual-fraction sum itself is not claimed as new.

The closest direct objective-class precursor found is:

- Jean-Christophe Pain, “Cumulative Riemann sums, distribution functions, and
  a universal inequality,” arXiv:2603.08959 (2026).

After reversing and normalizing the schedule, Pain's cumulative sum with
`g(s)=1/(rho+s)` is exactly `C_q` for `q>0`. It supplies an
order-independent integral upper bound, but it does not compare permutations
or identify an optimal order. Under the audit rubric, this is a score-2
partial precursor rather than a score-3 direct hit.

No score-3 to score-5 work was found for the following independent claim units:

- exact adjacent-swap identity and small-first/large-first extremizers;
- log-depletion majorization extremes;
- transformed-curvature classification and phase transition;
- precedence ideal dynamic program specialized to the objective and greedy
  counterexample;
- monotone-likelihood-ratio stochastic sorting theorem;
- multi-resource componentwise-chain theorem and residual-state context
  reversal.

The methods are classical: adjacent interchange, Karamata/majorization,
order-ideal dynamic programming, and monotone likelihood ratio. A referee could
treat individual parts as direct applications. The defensible novelty claim is
the permutation theory and the package built around one identity, not the
residual-ratio expression or interchange method in isolation.

## Submission status

As of 25 July 2026:

1. The manuscript was submitted as a Research Article to the
   **Journal of Scheduling** and is in the publisher's technical-check stage.
2. An arXiv submission was started in `math.OC` using the arXiv.org perpetual,
   non-exclusive license. The account's first-category endorsement is pending,
   so no arXiv identifier has been assigned.
3. The Journal of Scheduling submission opted into the publisher's Research
   Square preprint service. Public posting and DOI assignment remain pending.
4. An independent novelty check from at least one scheduling researcher and
   one majorization/stochastic-orders researcher remains desirable.
5. Optional strength upgrades for a future revision include:
   - hardness or approximation under precedence;
   - complexity of the heterogeneous network problem;
   - a calibrated case study where fraction/log shock is an actual operating cost.

The manuscript is formatted with the official Springer Nature class and the
`iicol` Journal of Scheduling layout. The current abstract has 177 words and
the manuscript has six keywords. A local Tectonic build and rendered-page
inspection pass; the submission system's exact `pdflatex` build remains to be
confirmed because `pdflatex` is not installed locally.

## Venue ranking (honest)

### Preferred preprint route
**arXiv**, categories `math.OC` (primary) and `cs.DS` or `math.PR` (secondary).

Why: open timestamp, no committee politics, full text public, and a priority
claim that does not wait on a referee queue. The pending first-category
endorsement currently prevents completion of this route.

### Best journal fit for the current draft
**Journal of Scheduling**

Why: the precedence, stochastic, and multi-resource sections are scheduling results, not pure inequality theory. Referees there already speak adjacent interchange, ideal DPs, and stochastic orders. Current length and package match better here than at a top general OR journal.

### Best short-note target if compressed hard
**Operations Research Letters**

Why: clean theorems, short format, less demand for a big application story. Requires cutting to the swap identity, majorization sandwich, phase transition, and maybe one extension (MLR or context reversal). Do not send the full multi-section draft as-is.

### Strong lineage / respectable OR home
**Naval Research Logistics**

Why: Smith's rule lives here historically. Residual-fraction sequencing is a natural NRL object. Slightly more application-tolerant than pure math journals.

### If the poset/network theory is expanded
**Discrete Applied Mathematics**

Why: width-parameterized ideal DP and network non-indexability fit DAM better once complexity or approximation is sharper.

### Do not aim here yet
- *Mathematics of Operations Research* / *Operations Research*: possible later only with hardness, approximation, or a serious application calibration. Elementary interchange + majorization alone will bounce.
- ML / NeurIPS-style venues: wrong audience.
- Prestige-chasing general science journals: wrong object.

## Recommended path

1. Complete the pending arXiv `math.OC` endorsement and source upload.
2. Confirm the Research Square preprint posting and DOI when issued.
3. Continue the specialist prior-art check while the journal performs its
   technical check.
4. Respond promptly to any technical-check or editorial requests from the
   **Journal of Scheduling**.

Current route: **Journal of Scheduling submitted, Research Square preprint
pending, and arXiv to follow after category endorsement.**
