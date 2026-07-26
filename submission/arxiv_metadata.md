# arXiv submission metadata

## Proposed classification

- Primary category: `math.OC` (Optimization and Control)
- Proposed cross-list: `cs.DS` (Data Structures and Algorithms)
- Journal reference: leave blank until accepted
- DOI: leave blank until assigned

## Title

Sequential Depletion Ordering with Residual-Fraction Costs

## Authors

Joseph L. Malone

## Abstract

A fixed collection of positive loads is removed sequentially from a resource
with permanent reserve \(q\ge 0\). If removing size \(x\) at residual level
\(R\) costs the residual fraction \(x/R\), the cost of a permutation \(\pi\) is
\[
C_q(\pi)=\sum_{k=1}^{n}
\frac{x_{\pi(k)}}{q+\sum_{j=k}^{n}x_{\pi(j)}}.
\]
An exact adjacent-interchange identity shows that nondecreasing size order
minimizes \(C_q\) and nonincreasing order maximizes it. For \(q>0\), the
log-depletion vector \(d_k=\log(R_k/R_{k+1})\) is majorized between the
decreasing-size and increasing-size extremes, so every Schur-convex functional
of \(d\) shares those extremes. The same comparison classifies nonlinear stage
costs \(\sum\phi(x_{\pi(k)}/R_k)\) by the curvature of
\(g(t)=\phi(1-e^{-t})\).

With precedence constraints, an order-ideal dynamic program computes the
optimum and smallest-available greedy can fail. For independent sizes totally
ordered by monotone likelihood ratio, the ex-ante small-first schedule
minimizes expected cost. For multi-resource loads, componentwise chains retain
the sorting rule, while heterogeneous vectors admit residual-state preference
reversals. Related residual-fraction sums appear in cumulative Riemann-sum
bounds; those results give order-independent integral estimates rather than a
permutation theory. The contribution is the ordering, majorization,
constrained, stochastic, and network analysis built on one interchange
identity.

## Comments

9 pages. Companion Python and Kotlin verification scripts available with the
source package.

## License

arXiv.org perpetual, non-exclusive license.

## Upload contents

The final upload archive should contain only:

- `sequential_depletion_ordering.tex`
- `sn-jnl.cls`
- `anc/sequential_depletion_verification.py`
- `anc/sequential_depletion_verification.main.kts`

The compiled PDF, Git metadata, submission notes, outreach material, priority
proofs, and build artifacts should not be placed in the arXiv source archive.
