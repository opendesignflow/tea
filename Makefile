
BRANCH_NAME?=$(shell git branch --show-current)

publish:
	@sbt publishM2