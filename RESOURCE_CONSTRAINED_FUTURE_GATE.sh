#!/bin/sh
# Future integrity and archival gate for the maintained sequential-depletion paper package.
set -eu

repo_name=sequential_depletion_paper_package
classification=maintained-non-application
repo_root=$(CDPATH= cd "$(dirname "$0")" && pwd -P)
verify=0
archive_dir=

die() {
    printf '%s\n' "future gate: $*" >&2
    exit 1
}

usage() {
    printf '%s\n' "usage: ./RESOURCE_CONSTRAINED_FUTURE_GATE.sh [--verify] [--archive-dir /absolute/existing/destination]"
}

while [ "$#" -gt 0 ]; do
    case "$1" in
        --verify) verify=1 ;;
        --archive-dir)
            shift
            [ "$#" -gt 0 ] || die "--archive-dir needs an absolute destination"
            archive_dir=$1
            ;;
        --help) usage; exit 0 ;;
        *) die "unknown option: $1" ;;
    esac
    shift
done

[ "$(basename "$repo_root")" = "$repo_name" ] || die "expected repository basename $repo_name"
git_root=$(git -C "$repo_root" rev-parse --show-toplevel 2>/dev/null) || die "not a Git worktree"
[ "$git_root" = "$repo_root" ] || die "script must remain at the Git worktree root"

if [ "$verify" -eq 1 ]; then
    [ -z "$(git -C "$repo_root" status --porcelain)" ] || die "a clean checkout is required"
    git -C "$repo_root" diff --check
    [ -f "$repo_root/sequential_depletion_ordering.tex" ] || die "LaTeX source is required"
    [ -f "$repo_root/sequential_depletion_ordering.pdf" ] || die "tracked PDF is required"
    [ -f "$repo_root/sequential_depletion_verification.py" ] || die "Python verifier is required"
    [ -f "$repo_root/sequential_depletion_verification.main.kts" ] || die "Kotlin verifier is required"
    command -v python3 >/dev/null 2>&1 || die "Python 3 is required"
    command -v kotlin >/dev/null 2>&1 || die "Kotlin is required"
    cd "$repo_root"
    python3 sequential_depletion_verification.py
    kotlin sequential_depletion_verification.main.kts
    printf '%s\n' "Numerical verification commands completed. They support transcription and debugging; the manuscript proofs remain authoritative."
fi

if [ -z "$archive_dir" ]; then
    [ "$verify" -eq 1 ] || usage
    exit 0
fi

case "$archive_dir" in
    /*) ;;
    *) die "archive destination must be absolute" ;;
esac
[ -d "$archive_dir" ] || die "archive destination must already exist"
archive_dir=$(CDPATH= cd "$archive_dir" && pwd -P)
case "$archive_dir" in
    /|/tmp) die "refusing a broad archive destination" ;;
esac
case "$repo_root/" in
    "$archive_dir/"*) die "archive destination must not contain the target repository" ;;
esac
case "$archive_dir/" in
    "$repo_root/"*) die "archive destination must not be inside the target repository" ;;
esac

head=$(git -C "$repo_root" rev-parse HEAD)
branch=$(git -C "$repo_root" symbolic-ref -q --short HEAD 2>/dev/null || printf '%s' detached)
package_dir=$archive_dir/$repo_name-$head.archive
[ ! -e "$package_dir" ] || die "archive package already exists: $package_dir"
umask 077
mkdir "$package_dir"

{
    printf 'classification=%s\n' "$classification"
    printf 'repository=%s\n' "$repo_root"
    printf 'head=%s\n' "$head"
    printf 'branch=%s\n' "$branch"
    printf '%s\n' 'scope=Git metadata and HEAD bundle only; working-tree, ignored, and untracked materials are omitted.'
    printf '%s\n' '[git-status-porcelain-v1-branch]'
    git -C "$repo_root" status --porcelain=v1 --branch
    printf '%s\n' '[cached-upstream]'
    git -C "$repo_root" rev-parse --abbrev-ref --symbolic-full-name '@{upstream}' 2>/dev/null || printf '%s\n' '(none)'
    printf '%s\n' '[cached-origin-head]'
    git -C "$repo_root" symbolic-ref -q --short refs/remotes/origin/HEAD 2>/dev/null || printf '%s\n' '(none)'
} > "$package_dir/git-metadata.txt"

git -C "$repo_root" bundle create "$package_dir/$repo_name-$head.bundle" HEAD
printf '%s\n' "Created metadata and a HEAD-only Git bundle. No submission, timestamp, verifier, or publication action was performed."
