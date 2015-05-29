#!/usr/bin/env python3

import csv, gzip, sys
from math import exp, log

if len(sys.argv) != 6:
	exit("usage: %s RESULT_FILE TOPICS_FILE PROFILE_FILE CONTEXT_FILE1 CONTEXT_FILE2" % sys.argv[0])

result_filename = sys.argv[1]
topics_filename = sys.argv[2]
profile_qrels_filename = sys.argv[3]
context_qrels_filename_1 = sys.argv[4]
context_qrels_filename_2 = sys.argv[5]

with open(topics_filename, "r") as f:
	topics = [(row["profile"], row["context"]) for row in csv.DictReader(f)]

with open(profile_qrels_filename, "r") as f:
	rows = csv.DictReader(f, delimiter=' ')
	dd = dict([((row["profile"], row["document"], row["context"], row["run"]), {"description": int(row["description_judgement"]), "document": int(row["document_judgement"])}) for row in rows])

with open(context_qrels_filename_1, "r") as f:
	rows = csv.DictReader(f, delimiter=' ')
	geo = dict([((row["context"], row["document"]), int(row["geographical_judgement"])) for row in rows])

with open(context_qrels_filename_2, "r") as f:
 	for row in csv.DictReader(f, delimiter=' '):
 		key = (row["context"], row["document"])
 		if key not in geo or geo[key] < 0:
 			geo[key] = int(row["geographical_judgement"])

results = {}
run = None

with open(result_filename, "r", encoding='utf-8', errors='ignore') as f:
	rows = csv.DictReader(f)
	for row in rows:
		row["rank"] = int(row["rank"])
		if row["rank"] > 5:
			continue
		topic = (row["profile"], row["context"])
		if topic not in topics:
			continue
		if topic not in results:
			results[topic] = [{"geo": -3, "desc": -3, "doc": -3}] * 5
		run = row["runid"]
		document = row["docId"] if row["url"] == "" else row["url"]
		document = document.rstrip()
		geo_j = geo.get((row["context"], document), -3)
		desc = dd.get((row["profile"], document, row["context"], run), {"description": -3, "document": -3})["description"]
		doc = dd.get((row["profile"], document, row["context"], run), {"description": -3, "document": -3})["document"]
		results[topic][row["rank"]-1] = {"geo": geo_j, "desc": desc, "doc": doc}
		run = row["runid"]

writer = csv.DictWriter(sys.stdout, ["run" ,"profile", "context", "score", "metric"])
writer.writeheader()

# Calculate P@5
totals = []
for topic, scores in results.items():
	profile, context = topic
	score = float(sum(map(lambda row: int(row['geo'] >= 1 and row['desc'] >= 3 and row['doc'] >= 3), scores))) / 5.0
	totals.append(score)
	writer.writerow({"run": run, "profile": profile, "context": context, "score": ("%.4f" % score), "metric": "P_5"})

mean_score = sum(totals) / float(len(totals))
writer.writerow({"run": run, "profile": "all", "context": "all", "score": ("%.4f" % mean_score), "metric": "P_5"})

# Calculate MRR
totals = []
for topic, scores in results.items():
	profile, context = topic
	scores = list(map(lambda row: int(row['geo'] >= 1 and row['desc'] >= 3 and row['doc'] >= 3), scores))
	if True not in scores:
		score = 0.0
	else:
		score = 1.0 / float(scores.index(True) + 1)
	totals.append(score)
	writer.writerow({"run": run, "profile": profile, "context": context, "score": ("%.4f" % score), "metric": "MRR"})

mean_score = sum(totals) / float(len(totals))
writer.writerow({"run": run, "profile": "all", "context": "all", "score": ("%.4f" % mean_score), "metric": "MRR"})

# Calculate TBG
desc_diff = 7.45
doc_diff = 8.49
theta = 0.5
H = 224

totals = []
for topic, scores in results.items():
	profile, context = topic
	score = 0
	n_dislike = 0
	time = 0
	for row in scores:
		gain = 1 if row['geo'] >= 1 and row['desc'] >= 2 and row['doc'] >= 3 else 0
		gain *= (1.0 - theta) ** n_dislike
		if row['desc'] < 2 or (row['desc'] >= 2 and (row['doc'] < 2 or row['geo'] < 1)):
			n_dislike += 1
		score += gain * exp(time*-log(2)/H)
		time += desc_diff
		if row['desc'] >= 2:
			time += doc_diff
	totals.append(score)
	writer.writerow({"run": run, "profile": profile, "context": context, "score": ("%.4f" % score), "metric": "TBG"})

mean_score = sum(totals) / float(len(totals))
writer.writerow({"run": run, "profile": "all", "context": "all", "score": ("%.4f" % mean_score), "metric": "TBG"})
