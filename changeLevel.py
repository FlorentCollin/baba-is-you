with open("lvl1.txt") as lvl:
    liste = []
    i = 0
    for line in lvl:
        if i==0:
            liste.append(line.strip()+"\n")
            i+=1
        else:
            line = line.strip().split()
            line = line[0] + " " + str(int(line[1])+1) + " " + str(int(line[2])+1) + "\n"
            liste.append(line)


with open("lvl1.txt", "w") as lvl:
    for line in liste:
        lvl.write(line)
